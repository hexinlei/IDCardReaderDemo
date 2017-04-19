package com.demo.ranger.idreaderdemo.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.preference.PreferenceActivity;

import com.demo.ranger.idreaderdemo.R;

import java.util.List;

/**
 * Created by hexinlei on 2017/4/18.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_header, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }
}
