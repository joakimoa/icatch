package com.absolutapp.icatch;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by otto on 2018-05-07.
 *
 * Links the InstructionsActivity to the ViewPager
 */

public class InstructionsAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    public int[] layouts;

    public InstructionsAdapter(int[] layouts, Context context)
    {
        this.layouts = layouts;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Returns how many slides there are
     * @return the length of the array containing the slides
     */
    @Override
    public int getCount(){
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    /**
     * Changes view to the correct slide
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = inflater.inflate(layouts[position], container, false);
        container.addView(view);

        return view;
    }

    /**
     * Destroys the previous view when the user changes slide
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        View view = (View) object;
        container.removeView(view);

    }
}
