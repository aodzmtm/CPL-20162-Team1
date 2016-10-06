package com.example.safelight;

import android.os.Bundle;
import android.preference.PreferenceActivity;

//import android.support.v7.app.ActionBarActivity;

//public class SettingActivity extends ActionBarActivity {
public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialog_pebble_uuid);
        addPreferencesFromResource(R.xml.setting_preference_screen);
    }

}
