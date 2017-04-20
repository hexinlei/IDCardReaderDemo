package com.demo.ranger.idreaderdemo.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.demo.ranger.idreaderdemo.R;

/**
 * Created by hexinlei on 2017/4/20.
 */
public class BackgroundFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_background_setting);
    }
}
