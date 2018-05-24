package com.absolutapp.icatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.graphics.ColorUtils;


public class GameActivity extends AppCompatActivity implements SensorEventListener{

    public static boolean BG_CLOSED;
    public static boolean GAME_WON;
    private TextView mTextMessage;
    private ImageView compass;
    private ArrowCalculator arrowCalculator;
  //  private Location myLocation = null;
    private Location goal;
    private float dirRelativeNorth = 0;
    private boolean debug = false;

    private final int closeEnoughDistance = 22;
    private boolean hasReachedGoal = false;
    private boolean GPSset = false;

    int startDebugCounter = 0;

    public LinearLayout[] dotsLayouts;
    public ImageView[] dots;
    private int[] dotsArray;
    private TimerAsyncTask timerTask;
    private ProgressBar[] progressBars;
    private int timerSeconds;

    private Vibrator vibrator;
    private boolean ticktock = false;
    private boolean lastTick = false;
    float currentDir= 0;


    private void closeEnough(Location location){
        if(debug && arrowCalculator.getDistance(location) <closeEnoughDistance){
            mTextMessage.setText("Close enougth " + arrowCalculator.getDistanceString(location));
        }
        if(!hasReachedGoal && arrowCalculator.getDistance(location) < closeEnoughDistance ){
            hasReachedGoal = true;
           startActivityForResult(new Intent(this, MainAccelerometer.class),1);

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    if(debug) {
                        debug = false;
                        mTextMessage.setTextSize(40);
                        startDebugCounter = 0;
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(++startDebugCounter>=4){
                        debug = true;
                        mTextMessage.setTextSize(12);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_game);
        mTextMessage = (TextView) findViewById(R.id.message);

        mTextMessage.setTextSize(40);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        compass = (ImageView) findViewById(R.id.compassImageView);

        // story dialog popup
        // DialogFragment dialog = new StoryDialog();
        Bundle args = new Bundle();
        startActivity(new Intent(this, StoryDialog.class));

        mTextMessage.setText("Waiting for GPS");
        gps();
        goal = new Location("");

        //SjönSjön
        //goal.setLongitude(13.208543d);
        //goal.setLatitude(55.710605d);

        //Parkeringsplats nära ute
        // 55.713374, 13.209769
        goal.setLatitude(55.713374);
        goal.setLongitude(13.209769);


        arrowCalculator = new ArrowCalculator(goal);

        onCreateCompass();

        if(GAME_WON){
            GAME_WON = false;
            onWon();
            finish();
        }
        // difficulty
        if (MainActivity.HARD_DIFFICULTY) {
            timerSeconds =  5;
        } else {
            timerSeconds = 100;
        }

        // loading bars and their AsyncTask (timerTask)
        progressBars = new ProgressBar[3];
        progressBars[0] = (ProgressBar) findViewById(R.id.progress1);
        progressBars[1] = (ProgressBar) findViewById(R.id.progress2);
        progressBars[2] = (ProgressBar) findViewById(R.id.progress3);

        int[] sec = new int[3];
        for (int i = 0; i < 3; i++) {
            sec[i] = timerSeconds + (i * 10);
            progressBars[i].setMax(sec[i]);
        }
        timerTask = new TimerAsyncTask(progressBars, sec, this);
      //  AsyncTaskTools.execute(timerTask); //Fyttad till när man får GPS


        // dots
        dotsArray = new int[]{0, 0, 0};

        dotsLayouts = new LinearLayout[3];
        dotsLayouts[0] = (LinearLayout) findViewById(R.id.dots1);
        dotsLayouts[1] = (LinearLayout) findViewById(R.id.dots2);
        dotsLayouts[2] = (LinearLayout) findViewById(R.id.dots3);

        updateDots(dotsLayouts[0], dotsArray[0]);
        updateDots(dotsLayouts[1], dotsArray[1]);
        updateDots(dotsLayouts[2], dotsArray[2]);

        vibrator = (Vibrator) getBaseContext().getSystemService(getBaseContext().VIBRATOR_SERVICE);

    }

    // updates the dots
    public void gameTimerUpdate(int n) {
        dotsArray[n]++;
        updateDots(dotsLayouts[n], dotsArray[n]);

        for (int i = 0; i < dotsArray.length; i++) {
            if (dotsArray[i] >= 3) {
                onDefeat();
                System.out.println("Game over");
                return;
            }
        }
        int sec = timerSeconds + (n * 10);
        progressBars[n].setProgress(0);
        progressBars[n].setMax(sec);
       // timerTask[n] = new TimerAsyncTask(progressBars[n], sec, this, n);
        timerTask.reset(n);
        //AsyncTaskTools.execute(timerTask[n]);
    }

    private void updateDots(LinearLayout ll, int filledDots) {
        if(ll != null) {
            ll.removeAllViews();
        }

        int noDots = 3;

        dots = new ImageView[noDots];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(4, 0, 4, 0);

        if (filledDots == 0) {
            for (int i = 0; i < noDots; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
                ll.addView(dots[i], params);
            }
        } else {
            for (int i = 0; i < noDots; i++) {
                dots[i] = new ImageView(this);

                if (i <= filledDots-1) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
                    dots[i].setColorFilter(0xffff4081);
                } else {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
                }
                ll.addView(dots[i], params);
            }
        }
    }



    protected void rotateArrow(float deg) {
        compass.setRotation(deg);
        currentDir = deg;
    }

    /**Sets color from green = closeEnougthDistance to red = 100*/
    protected void colorArrow(float colorGradient) {


        if(colorGradient > 100){
            colorGradient = 0;
        }
        else if(colorGradient < 0){
            colorGradient = 100;
        }
        else{
            colorGradient = 100-colorGradient;
        }
        float hue = (colorGradient-closeEnoughDistance) / (100-closeEnoughDistance) * 120;
        int col = ColorUtils.HSLToColor(new float[]{colorGradient, 1, 0.5f});
        compass.setColorFilter(col);
    }


    private LocationManager locationManager;
    private LocationListener locationListener;

    private String getLatLong(Location location){
        return "Lat: " + location.getLatitude() + " Long: " + location.getLongitude();
    }

    private String getRelativeLatLong(Location location){
        return " Relative lat: " + (location.getLatitude()- goal.getLatitude()) + " relative long: " + (location.getLongitude()- goal.getLongitude());
     }

    private void gps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(startDebugCounter>0){
                    startDebugCounter--;
                }
                if(!GPSset){
                    GPSset = true;
                    compass.setImageDrawable(getDrawable(R.drawable.ic_arrow_upward_black_24dp));

                }
                if(BG_CLOSED){
                    BG_CLOSED = false;
                  //  initiateTimers();
                    AsyncTaskTools.execute(timerTask);
                }
                //we want
                if(debug) {
                    mTextMessage.setText(arrowCalculator.getDistanceString(location) + "\n\n" +getLatLong(location) + getRelativeLatLong(location) + " direction " + arrowCalculator.getDirection(location));
                } else{
                    mTextMessage.setText(arrowCalculator.getDistanceString(location));
                }
                dirRelativeNorth = arrowCalculator.getDirection(location);
                colorArrow(arrowCalculator.getDistance(location));
                closeEnough(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {


            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                // if gps off -> to settings.s
                Log.d("No gps","no gps");
            }
        };

        // Getting Current Location
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},10);



                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
        }//else {
                 startGPS();
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if ((grantResults.length>0) && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startGPS();
                }
                return;
        }
    }


    @SuppressLint("MissingPermission") //THis is solved.
    private void startGPS(){
        locationManager.requestLocationUpdates("gps", 200, 0, locationListener);
    }



/** From compass*/
private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    int mAzimuth;

    private void onCreateCompass() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    private void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }


    public void stop() {
        if(haveSensor && haveSensor2){
            mSensorManager.unregisterListener(this,mAccelerometer);
            mSensorManager.unregisterListener(this,mMagnetometer);
        }
        else{
            if(haveSensor)
                mSensorManager.unregisterListener(this,mRotationV);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        if(GPSset) {
            setNorth(-mAzimuth%360);

            float dir = (currentDir+360*2)%360;

           Log.d("CurrentDir", "onSensorChanged: " + dir);
            if(dir > 30 && dir < 330){
                if(vibrator.hasVibrator()){
                    if(ticktock != lastTick) {
                        lastTick = ticktock;

                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(VibrationEffect.createOneShot(10, 70));
                        } else {
                            vibrator.vibrate(10);
                        }

                    }
                }

            }




        }







    }

    private void setNorth(int north) {
        rotateArrow(north + dirRelativeNorth);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void testFramme(View view) {
        startActivityForResult(new Intent(this, MainAccelerometer.class),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("OnActivityResult", "onActivityResult: " + "request: " + requestCode  +" result: " + resultCode);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            mTextMessage.setText("WON!!!");
            onWon();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onWon(){
        //for (TimerAsyncTask timerAsyncTask : timerTask){
            timerTask.cancel(true);
        //}
        startActivity(new Intent(this, VictoryActivity.class));
        finish();
    }

    private void onDefeat() {
       // for (TimerAsyncTask timerAsyncTask : timerTask){
         timerTask.cancel(true);
        //}
        startActivity(new Intent(this, LosingScreen.class));
        finish();
    }

    public void tick() {
        ticktock = !ticktock;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timerTask.cancel(true);
        startService(new Intent(this, MusicService.class));
    }
}



