package com.markwy.onyourbike;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

public class SettingsActivity extends Activity {

    private static String CLASS_NAME;

    private CheckBox vibrate;
    private CheckBox stayAwake;

    public void SettingsActivity() {
        CLASS_NAME = getClass().getName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings); // write activity_setting directly not work

        Log.d(CLASS_NAME, "setting activity onCreate");
        vibrate = (CheckBox) findViewById(R.id.vibrate_checkbox);

        //stayAwake = (CheckBox) findViewById(R.id.awake_checkbox);

        Settings settings = ((OnYourBike) getApplication()).getSettings();

        vibrate.setChecked(settings.isVibrateOn(this));
        //stayAwake.setChecked(settings.isCaffeinated(this));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(CLASS_NAME, "onStop");

        Settings settings = ((OnYourBike) getApplication()).getSettings();

        Log.i(CLASS_NAME, "Saving settings");
        settings.setVibrate(this, vibrate.isChecked());
        //settings.setCaffeinated(this, stayAwake.isChecked());
    }
}
