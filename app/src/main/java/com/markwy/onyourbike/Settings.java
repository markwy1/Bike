package com.markwy.onyourbike;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by dryon on 2017/12/31.
 */

public class Settings {
    private static String CLASS_NAME;
    private static String VIBRATE = "vibrate";

    /**
     * where is the auto completion?
     */
    protected boolean vibrateOn;

    public Settings() {
        CLASS_NAME = getClass().getName();
    }

    public boolean isVibrateOn(Activity activity) {
        Log.d(CLASS_NAME, "isVibrateOn");
        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        return vibrateOn;
    }

    public void setVibrate(Activity activity, boolean vibrate) {
        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(VIBRATE, vibrate);
        editor.apply();
              vibrateOn = vibrate;
    }



}
