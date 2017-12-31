package com.markwy.onyourbike;

//main form entry

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.markwy.onyourbike.R.layout.activity_main;

//import android.support.v7.app.AppCompatActivity;


public class TimerActivity extends Activity {

    //static String LogClass;
    private static String CLASS_NAME;
    //private static final String CLASS_NAME = "CLASS_NAME";
    private static long UPDATE_EVERY = 500;
    protected TextView counter;
    protected Button start;
    protected Button stop;
    protected boolean timerRunning;
    protected long startedAt;
    protected long lastStopped;
    protected Vibrator vibrator;
    protected long lastSeconds;


    public TimerActivity() { CLASS_NAME = getClass().getName(); }


    protected Handler handler;
    protected UpdateTimer updateTimer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        counter = (TextView) findViewById(R.id.timer);
        start = (Button) findViewById(R.id.start_button);
        stop = (Button) findViewById(R.id.stop_button);

        timerRunning = false;
        enableButtons();


        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        //TextView hello = (TextView) findViewById(R.id.hello);
        Log.d(CLASS_NAME, "onCreate");
        //hello.setText("生命在于运动，go");
    }



    protected void enableButtons() {
        Log.d(CLASS_NAME, "enable_button");
        start.setEnabled(!timerRunning);
        stop.setEnabled(timerRunning);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(CLASS_NAME, "show menu.");
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;

    }

    /**
     * Called when the Activity starts.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(CLASS_NAME, "onStart");
        if(timerRunning) {
            handler = new Handler();
            updateTimer = new UpdateTimer();
            handler.postDelayed(updateTimer, UPDATE_EVERY);
        }

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator == null) {
            Log.w(CLASS_NAME, "No vibration service exists.");
        } else {
            Log.w(CLASS_NAME, "Vibration service exists.");
        }
    }

    /**
     * Called when the Activity is resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(CLASS_NAME, "onResume");

        enableButtons();
        setTimeDisplay();
    }

    /**
     * Called when the Activity is paused.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(CLASS_NAME, "onPause");
    }

    /**
     * Called when the Activity is stopped.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.d(CLASS_NAME, "onStop");
        if (timerRunning) {
            handler.removeCallbacks(updateTimer);
            updateTimer = null;
            handler = null;
        }

    }

    /**
     * Called when the Activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(CLASS_NAME, "onDestroy");
    }

    /**
     * Called when the Activity is restarted.
     */
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(CLASS_NAME, "onRestart");
    }

    public void clickedStart(View view) {
        Log.d(CLASS_NAME, "Clicked start button.");
        timerRunning = true;
        enableButtons();
        startedAt = System.currentTimeMillis();
        setTimeDisplay();

        handler = new Handler ();
        updateTimer = new UpdateTimer();
        handler.postDelayed(updateTimer, UPDATE_EVERY);

        vibratePhone();
    }

    public void clickedStop(View view) {
        Log.d(CLASS_NAME, "Clicked stop button.");
        timerRunning = false;
        enableButtons();
        lastStopped = System.currentTimeMillis();
        setTimeDisplay();

        handler.removeCallbacks(updateTimer);
        handler = null;
    }

    public void clickedSettings(View view) {
        Log.d(CLASS_NAME, "ClickedSettings");
        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(settingsIntent);
    }

    protected void setTimeDisplay() {
        String display;
        long timeNow;
        long diff;
        long seconds;
        long minutes;
        long hours;

        //Log.d(CLASS_NAME, "Setting the time display");

        if (timerRunning) {
            timeNow = System.currentTimeMillis();
        } else {
            timeNow = lastStopped;
        }

        diff = timeNow - startedAt;

        if (diff < 0) {
            diff = 0;
        }

        seconds = diff/1000;
        minutes = seconds/60;
        hours = minutes/60;
        seconds = seconds % 60;
        minutes = minutes % 60;

        display = String.format("%d", hours) + ":"
                + String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds);

        counter.setText(display);
    }

    // put the class within the class
    class UpdateTimer implements Runnable {

        public void run () {
            //Log.d("Run", "implements run");
            setTimeDisplay();
            if (timerRunning) {
                vibrateCheck();
            }
            if (handler != null) {
                handler.postDelayed(this, UPDATE_EVERY);
            }

        }

        //put into UpdaterTimer
        protected void vibrateCheck() {
            long timeNow = System.currentTimeMillis();
            long diff = timeNow - startedAt;
            long vseconds = diff / 1000;
            long vminutes = vseconds / 60;

            vseconds = vseconds % 60;
            vminutes = vminutes % 60;

            if (vibrator != null && vseconds == 0 && vseconds != lastSeconds) {
                long[] once = {0, 100};
                long[] twice = {0, 100, 400, 100};
                long[] thrice = {0, 100, 400, 100, 400, 100};

                Log.d(CLASS_NAME, String.format("%d", vminutes) +":" + String.format("%d", vseconds));
                //every hour
                if (vminutes == 0) {
                    Log.i(CLASS_NAME, "Vibrate 3 times");
                    vibrator.vibrate(thrice, -1);
                }

                //every 15 minutes
                else if (vminutes % 5 == 0) {
                    Log.i(CLASS_NAME, "Vibrate 2 times");
                    vibrator.vibrate(twice, -1);
                }

                //every hour
                else if (vseconds % 30 == 0) {
                    Log.i(CLASS_NAME, "Vibrate once");
                    vibrator.vibrate(once, -1);
                }

                lastSeconds = vseconds;

            }
        }

    }

    protected void vibratePhone() {
        long[] once = {0, 100, 100, 100}; //wait 0, vibrate 100ms, wait 100, vibrate 100ms
        if (vibrator != null) {
            vibrator.vibrate(once, -1); // -1, not repeat
        }
    }

}

