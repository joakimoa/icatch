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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.graphics.ColorUtils;


public class GameActivity extends AppCompatActivity implements SensorEventListener{


    private TextView mTextMessage;
    private ImageView compass;
   // private GPS gps;
    private ArrowCalculator arrowCalculator;
    private Location myLocation = null;
    private Location goal;
//    private int northDir = 0;
    private float dirRelativeNorth = 0;
    private boolean debug = false;

    private final int closeEnoughDistance = 22;
    private boolean hasReachedGoal = false;
    private boolean GPSset = false;

    int startDebugCounter = 0;



    private void closeEnough(Location location){
        if(debug && arrowCalculator.getDistance(location) <closeEnoughDistance){
            mTextMessage.setText("Close enougth " + arrowCalculator.getDistanceString(location));
        }
        if(!hasReachedGoal && arrowCalculator.getDistance(location) < closeEnoughDistance ){
            hasReachedGoal = true;
           startActivityForResult(new Intent(this, MainAccelerometer.class),1);

        }
    }

//    private MapView mapView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);

                    //   rotateArrow(90);
                    //rotateArrow(90);
                    //mTextMessage.setText(((Float) arrowCalculator.getDistance()).toString());

                    if(debug) {
                        debug = false;
                        mTextMessage.setTextSize(40);
                        startDebugCounter = 0;
                    }
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                   // colorArrow(60);

                   // Location myLocation = arrowCalculator.getMyLocation();
                 //   if (myLocation != null) {
                   //     mTextMessage.setText("Lat: " + myLocation.getLatitude() + " Long: " + myLocation.getLongitude());
//
  //                  } else {
     //                   mTextMessage.setText("No known location");
       //             }
                    //debug = true;

                    //  setResult(1);;
                    if(++startDebugCounter>=4){
                        debug = true;
                        mTextMessage.setTextSize(12);
                    }
                  /*  if(startDebugCounter>2 && startDebugCounter<2) {
                        Toast.makeText(getBaseContext(), "Tap " + (4 - startDebugCounter) + " times to start debug", Toast.LENGTH_SHORT).show();
                    }*/
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mTextMessage = (TextView) findViewById(R.id.message);

        mTextMessage.setTextSize(40);
        //mTextMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //     mapView = (MapView) findViewById(R.id.mapView);
        compass = (ImageView) findViewById(R.id.compassImageView);

        // story dialog popup
        // DialogFragment dialog = new StoryDialog();
        Bundle args = new Bundle();
        //args.putString(YesNoDialog.ARG_TITLE, title);
        //args.putString(YesNoDialog.ARG_MESSAGE, message);
        //int yesno = 0;
     //   dialog.setArguments(args);
        //dialog.setTargetFragment(this, yesno);
       // dialog.show(getFragmentManager(), "tag");
        startActivity(new Intent(this, StoryDialog.class));

        //gps = new GPS(this);

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



    }



    protected void rotateArrow(float deg) {
        //compass.setRotation(compass.getRotation() + deg);
        compass.setRotation(deg);
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
        //compass.setColorFilter();
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
                    //compass.setImageDrawable(getDrawable(R.drawable.ic_gps_not_fixed_black_24dp));
                    compass.setImageDrawable(getDrawable(R.drawable.ic_arrow_upward_black_24dp));
                }
                //we want
                if(debug) {
                    mTextMessage.setText(arrowCalculator.getDistanceString(location) + "\n\n" +getLatLong(location) + getRelativeLatLong(location) + " direction " + arrowCalculator.getDirection(location));
                } else{
                    mTextMessage.setText(arrowCalculator.getDistanceString(location));
                }
                /// / rotateArrow(arrowCalculator.getDirection(myLocation));
               // mTextMessage.setText(getLatLong(location) + getRelativeLatLong(location) + " direction " + arrowCalculator.getDirection(location));
               // rotateArrow(arrowCalculator.getDirection(myLocation));
             //   rotateArrow(arrowCalculator.getDirection(location));
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
        //String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        // Location location = locationManager.getLastKnownLocation(provider);
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
     //   compass_img.setRotation(-mAzimuth);
        //northDir = -mAzimuth;
        if(GPSset) {
            setNorth(-mAzimuth);
        }
        //
//  String where = "NW";
//
//        if (mAzimuth >= 350 || mAzimuth <= 10) {
//            where = "N";
//            //if (vibrator.hasVibrator()){
//              //  vibrator.vibrate(10);
//           // }
//        }
//        else if (mAzimuth < 350 && mAzimuth > 280)
//            where = "NW";
//        else if (mAzimuth <= 280 && mAzimuth > 260)
//            where = "W";
//        else if (mAzimuth <= 260 && mAzimuth > 190)
//            where = "SW";
//        else if (mAzimuth <= 190 && mAzimuth > 170)
//            where = "S";
//        else if (mAzimuth <= 170 && mAzimuth > 100)
//            where = "SE";
//        else if (mAzimuth <= 100 && mAzimuth > 80)
//            where = "E";
//        else if (mAzimuth <= 80 && mAzimuth > 10)
//            where = "NE";
//
//
//        txt_compass.setText(mAzimuth + "° " + where);
    }

    private void setNorth(int north) {
        rotateArrow(north + dirRelativeNorth);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void testFramme(View view) {
        startActivityForResult(new Intent(this, MainAccelerometer.class),1);

     //   finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("OnActivityResult", "onActivityResult: " + "request: " + requestCode  +" result: " + resultCode);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            mTextMessage.setText("WON!!!");
          //  compass.setImageDrawable(getDrawable(R.drawable.ic_hourglass_empty_black_24dp));
            onWon();
        }
        //if(requestCode == 2 && resultCode == Activity.RESULT_OK){
         //   mTextMessage.setText("WON!!!");
        //}
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void onWon(){
        //Denna körs när man vinner. Använd för att öppna vinnar-skärm
    }
}



