/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lge.settings.device;

import android.content.ComponentName;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.EditTextPreference;
import android.preference.SwitchPreference;
import android.preference.TwoStatePreference;
import android.widget.ListView;
import android.widget.Toast;

import com.lge.settings.device.Battery;
import com.lge.settings.device.utils.Constants;
import com.lge.settings.device.utils.NodePreferenceActivity;
import com.lge.settings.device.utils.PreferenceHelper;

public class DeviceSettings extends NodePreferenceActivity {

    private static final String SPECTRUM_CATEGORY_KEY = "spectrum_category";

    private SwitchPreference mFCSwitch;
	private EditTextPreference cyclePreference;
    private EditTextPreference tempPreference;
    private EditTextPreference healthPreference;
    private SwitchPreference mDaylightModeSwitch;
    private ListPreference mSpectrum;
    private PreferenceCategory mSpectrumCategory;
    private SwitchPreference mSpectrumSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.device_settings);

        ListView lv = getListView();
        lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lv.setDividerHeight(0);

        mFCSwitch = (SwitchPreference) findPreference(Constants.KEY_FAST_CHARGE);
        mFCSwitch.setEnabled(FastChargeSwitch.isSupported());
        mFCSwitch.setChecked(FastChargeSwitch.isCurrentlyEnabled(this));
        mFCSwitch.setOnPreferenceChangeListener(new FastChargeSwitch());

        cyclePreference = (EditTextPreference) findPreference(Constants.KEY_BATTERY_CYCLE);
        tempPreference = (EditTextPreference) findPreference(Constants.KEY_BATTERY_TEMP);
        healthPreference = (EditTextPreference) findPreference(Constants.KEY_BATTERY_HEALTH);

        cyclePreference.setSummary(Battery.getBatteryCycle());
        tempPreference.setSummary(Battery.getBatteryTemp()+"°C");
        healthPreference.setSummary(Battery.getBatteryHealth());

        mDaylightModeSwitch = (SwitchPreference) findPreference(Constants.KEY_DLM_SWITCH);
        mDaylightModeSwitch.setEnabled(DaylightModeSwitch.isSupported());
        mDaylightModeSwitch.setChecked(DaylightModeSwitch.isCurrentlyEnabled(this));
        mDaylightModeSwitch.setOnPreferenceChangeListener(new DaylightModeSwitch());

        mSpectrumSwitch = (SwitchPreference) findPreference(Constants.SPECTRUM_SWITCH_KEY);
        mSpectrumSwitch.setEnabled(true);
        mSpectrumSwitch.setChecked(!PreferenceHelper.isSpectrumEnabled(getApplicationContext()));
        mSpectrumSwitch.setOnPreferenceChangeListener(this);

        mSpectrum = (ListPreference) findPreference(Constants.SPECTRUM_KEY);
        if( mSpectrum != null ) {
            mSpectrum.setEnabled(PreferenceHelper.isSpectrumEnabled(getApplicationContext()));
            mSpectrum.setValue(SystemProperties.get(Constants.SPECTRUM_SYSTEM_PROPERTY, "0"));
            mSpectrum.setOnPreferenceChangeListener(this);
        }

        mSpectrumCategory = (PreferenceCategory) findPreference(SPECTRUM_CATEGORY_KEY);
        if (!getResources().getBoolean(R.bool.device_supports_spectrum)) {
            getPreferenceScreen().removePreference(mSpectrumCategory);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        if (Constants.SPECTRUM_KEY.equals(key) && PreferenceHelper.isSpectrumEnabled(getApplicationContext())) {
            String strvalue = (String) newValue;
            SystemProperties.set(Constants.SPECTRUM_SYSTEM_PROPERTY, strvalue);
            return true;
        } else if(Constants.SPECTRUM_SWITCH_KEY.equals(key)) {
            Boolean enabled = (Boolean) newValue;
            if(enabled) {
                SystemProperties.set(Constants.SPECTRUM_SYSTEM_PROPERTY, "");
                SystemProperties.set(Constants.SPECTRUM_SUPPORT_SYSTEM_PROPERTY, "0");
                PreferenceHelper.setSpectrumEnabled(getApplicationContext(), false);
                mSpectrum.setEnabled(false);
            } else {
                SystemProperties.set(Constants.SPECTRUM_SYSTEM_PROPERTY, "0");
                SystemProperties.set(Constants.SPECTRUM_SUPPORT_SYSTEM_PROPERTY, "1");
                mSpectrum.setEnabled(true);
                PreferenceHelper.setSpectrumEnabled(getApplicationContext(), true);
            }

            Toast.makeText(getApplicationContext(), getString(R.string.toast_restart), Toast.LENGTH_LONG).show();

            return true;
        }
        return super.onPreferenceChange(preference, newValue);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If running on a phone, remove padding around the listview
        getListView().setPadding(0, 0, 0, 0);

        // mHapticFeedback.setChecked(
        //         Settings.System.getInt(getContentResolver(), KEY_HAPTIC_FEEDBACK, 1) != 0);
    }
}
