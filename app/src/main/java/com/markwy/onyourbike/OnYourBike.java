package com.markwy.onyourbike;

import android.app.Application;

/**
 * Created by dryon on 2017/12/31.
 */

public class OnYourBike extends Application {
    protected Settings settings;

    /**
     * Returns the application settings, creates the settings if they don't
     * exits.
     *
     * @return settings
     */
    public Settings getSettings() {
        if (settings == null) {
            settings = new Settings();
        }

        return settings;
    }

    /**
     * Sets the application settings.
     *
     * @param settings
     * application settings
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}