package com.absolutapp.icatch;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class keeps track of if the user has opened the app before.
 * Only shows the slide-show if it's the first time the app is opened (won't be true for us because of testing purposes)
 * Created by otto on 2018-05-08.
 */

public class PreferenceManager {

    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context)
    {
        this.context = context;
        getSharedPreference();
    }

    /**
     * Gets the shared preferences
     */
    private void getSharedPreference()
    {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_preference), Context.MODE_PRIVATE);
    }

    /**
     * Writes to the shared preferences, marking the app has been opened
     */
    public void writePreference()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.my_preference_key), "INIT_OK");
        editor.commit();
    }

    /**
     * Checks if the app has been opened before and changes the preferences accordingly
     * @return
     */
    public boolean checkPreference()
    {
        boolean status = false;

        if(sharedPreferences.getString(context.getString(R.string.my_preference_key), "null").equals("null"))
        {
            status = false;
        }
        else
        {
            status = false; //set this to true if you want the class to work
        }
        return  status;
    }

    /**
     * Clears the preferences if the user wants to see the slideshow again
     */
    public void clearPreference()
    {
        sharedPreferences.edit().clear().commit();
    }

}
