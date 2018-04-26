package com.absolutapp.icatch;

import android.graphics.Color;
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
                    rotateArrow(90);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    colorArrow(60);

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


