package com.absolutapp.icatch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * A class showing the how-to-play tutorial
 * Built by following this guide: https://www.youtube.com/watch?v=wzh2vojv9m8&list=PLshdtb5UWjSoyfVybSD_sLVbT1w_CV2e5
 */
public class InstructionsActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private int[] layouts = {
        R.layout.instructionslide_1,
        R.layout.instructionslide_2,
        R.layout.instructionslide_3,
        R.layout.instructionslide_4
    };

    private InstructionsAdapter adapter;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    private Button BnNext, BnSkip;

    /**
     * Created the view and all the components
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //checks if the user has opened the app before
        if(new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

        if(Build.VERSION.SDK_INT >= 19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_instructions);
        mPager = findViewById(R.id.viewPager);
        adapter = new InstructionsAdapter(layouts, this);
        mPager.setAdapter(adapter);

        Dots_Layout = findViewById(R.id.dotsLayout);
        createDots(0);

        BnNext = findViewById(R.id.bnNext);
        BnSkip = findViewById(R.id.bnSkip);
        BnNext.setOnClickListener(this);
        BnSkip.setOnClickListener(this);

        /**
         * Changes the layout of the buttons depending on what slide currently is showing
         */
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * Decides the layout of the buttons
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if(position==layouts.length-1)
                {
                    BnNext.setText("Start");
                    BnSkip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    BnNext.setText("Next");
                    BnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * Creates the slideshow-dots and highlights them depending on what page is currently active
     * @param currentPosition
     */
    private void createDots(int currentPosition)
    {
        if(Dots_Layout != null)
        {
            Dots_Layout.removeAllViews();
        }

        dots = new ImageView[layouts.length];

        for (int i = 0; i < layouts.length; i++)
        {
            dots[i] = new ImageView(this);
            if(i==currentPosition)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(4, 0, 4, 0);

            Dots_Layout.addView(dots[i], params);
        }
    }

    /**
     * This function is called whenever the user clicks a button.
     * Either changes slide or loads the main menu
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.bnNext:
                loadNextSlide();
                break;

            case R.id.bnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }


    }

    /**
     * Loads the main menu
     */
    private void loadHome()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Loads the next slide
     */
    private void loadNextSlide()
    {
        int next_slide = mPager.getCurrentItem()+1;

        if(next_slide < layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
}
