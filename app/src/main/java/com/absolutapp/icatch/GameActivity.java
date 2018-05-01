package com.absolutapp.icatch;

import android.app.DialogFragment;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.graphics.ColorUtils;

public class GameActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageView compass;
    private GPS gps;
    private ArrowCalculator arrowCalculator;

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
                    mTextMessage.setText(((Float)arrowCalculator.getDistance()).toString());
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    colorArrow(60);

                    Location myLocation = arrowCalculator.getMyLocation();
                    if(myLocation != null) {
                        mTextMessage.setText("Lat: " + myLocation.getLatitude() + " Long: " + myLocation.getLongitude());

                    }
                    else{
                        mTextMessage.setText("No known location");
                    }
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


    protected void rotateArrow(float deg){
        compass.setRotation(compass.getRotation() + deg);

    }

    /**Sets cclor from red = 0 to green = 100*/
    protected void colorArrow(float colorGradient){
        float hue = colorGradient/100*120;
        int col = ColorUtils.HSLToColor(new float[]{colorGradient,1,0.5f});
        compass.setColorFilter(col);
    //compass.setColorFilter();
    }
}


