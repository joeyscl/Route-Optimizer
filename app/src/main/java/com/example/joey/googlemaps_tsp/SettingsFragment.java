package com.example.joey.googlemaps_tsp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Joey on 2015-08-02.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(0xffeeeeee);
    }

}