package com.absolutapp.icatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v4.graphics.ColorUtils;

import com.google.android.gms.maps.MapView;

public class GameActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageView compass;
    private GPS gps;
    private ArrowCalculator arrowCalculator;
    // private Location my

//    private MapView mapView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    //   rotateArrow(90);
                    rotateArrow(arrowCalculator.getDirection());
                    mTextMessage.setText(((Float) arrowCalculator.getDistance()).toString());
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    colorArrow(60);

                    Location myLocation = arrowCalculator.getMyLocation();
                 //   if (myLocation != null) {
                   //     mTextMessage.setText("Lat: " + myLocation.getLatitude() + " Long: " + myLocation.getLongitude());
//
  //                  } else {
     //                   mTextMessage.setText("No known location");
       //             }
                        gps();
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //     mapView = (MapView) findViewById(R.id.mapView);
        compass = (ImageView) findViewById(R.id.compassImageView);

        // story dialog popup
        DialogFragment dialog = new StoryDialog();
        Bundle args = new Bundle();
        //args.putString(YesNoDialog.ARG_TITLE, title);
        //args.putString(YesNoDialog.ARG_MESSAGE, message);
        int yesno = 0;
        dialog.setArguments(args);
        //dialog.setTargetFragment(this, yesno);
        dialog.show(getFragmentManager(), "tag");

        gps = new GPS(this);
        Location SjonSjon = new Location("");
        SjonSjon.setLongitude(13.208543d);
        SjonSjon.setLatitude(55.710605d);
        arrowCalculator = new ArrowCalculator(gps, SjonSjon);

    }


    protected void rotateArrow(float deg) {
        compass.setRotation(compass.getRotation() + deg);

    }

    /**Sets cclor from red = 0 to green = 100*/
    protected void colorArrow(float colorGradient) {
        float hue = colorGradient / 100 * 120;
        int col = ColorUtils.HSLToColor(new float[]{colorGradient, 1, 0.5f});
        compass.setColorFilter(col);
        //compass.setColorFilter();
    }


    private LocationManager locationManager;
    private LocationListener locationListener;

    private void gps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //we want
                mTextMessage.setText("Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
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
        }else {
                 startGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if ((grantResults.length>0) &&   grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startGPS();
                }
                return;
        }
    }


    @SuppressLint("MissingPermission") //THis is solved.
    private void startGPS(){
        locationManager.requestLocationUpdates("gps", 200, 0, locationListener);
    }
}


