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

package com.lge.settings.device.utils;

import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Constants {

    // Fast charge
    public static final String KEY_FAST_CHARGE = "fast_charge";

    //Battery
    public static final String BATTERY_CYCLE_NODE = "/sys/class/power_supply/bms/battery_cycle";
    public static final String BATTERY_HEALTH_NODE = "/sys/class/power_supply/battery/health";
    public static final String BATTERY_TEMP_NODE = "/sys/class/power_supply/bms/temp";
    public static final String BATTERY_TYPE_NODE = "/sys/class/power_supply/bms/battery_type";
    public static final String BATTERY_PACK_NAME_NODE = "/sys/class/lge_power/lge_batt_id/batt_pack_name";

    public static final String KEY_BATTERY_CYCLE = "battery_cycle";
    public static final String KEY_BATTERY_TEMP = "battery_temp";
    public static final String KEY_BATTERY_HEALTH = "battery_health";

    // Gestures nodes
    public static final String TOUCHSCREEN_CAMERA_NODE = "/proc/touchpanel/letter_o_enable";
    public static final String TOUCHSCREEN_DOUBLE_SWIPE_NODE = "/proc/touchpanel/double_swipe_enable";
    public static final String TOUCHSCREEN_LEFT_ARROW = "/proc/touchpanel/left_arrow_enable";
    public static final String TOUCHSCREEN_RIGHT_ARROW = "/proc/touchpanel/right_arrow_enable";
    public static final String TOUCHSCREEN_FLASHLIGHT_NODE = "/proc/touchpanel/down_arrow_enable";

    // Array of music-related gestures
    public static final String[] TOUCHSCREEN_MUSIC_GESTURES_ARRAY = {TOUCHSCREEN_DOUBLE_SWIPE_NODE, TOUCHSCREEN_LEFT_ARROW, TOUCHSCREEN_RIGHT_ARROW};

    // Gestures nodes default values
    public static final boolean TOUCHSCREEN_CAMERA_DEFAULT = true;
    public static final boolean TOUCHSCREEN_MUSIC_DEFAULT = true;
    public static final boolean TOUCHSCREEN_FLASHLIGHT_DEFAULT = true;

    // Alert slider nodes
    public static final String NOTIF_SLIDER_TOP_NODE = "/proc/tri-state-key/keyCode_top";
    public static final String NOTIF_SLIDER_MIDDLE_NODE = "/proc/tri-state-key/keyCode_middle";
    public static final String NOTIF_SLIDER_BOTTOM_NODE = "/proc/tri-state-key/keyCode_bottom";

    // Spectrum modes
    public static final String SPECTRUM_KEY = "spectrum";
    public static final String SPECTRUM_SWITCH_KEY = "spectrum_switch";
    public static final String SPECTRUM_SUPPORT_SYSTEM_PROPERTY  = "spectrum.support";
    public static final String SPECTRUM_SYSTEM_PROPERTY = "persist.spectrum.profile";

    // Display modes
    public static final String KEY_DLM_SWITCH = "dlm";

    // Holds <preference_key> -> <proc_node> mapping
    public static final Map<String, String> sBooleanNodePreferenceMap = new HashMap<>();
    public static final Map<String, String> sStringNodePreferenceMap = new HashMap<>();

    // Holds <preference_key> -> <default_values> mapping
    public static final Map<String, Object> sNodeDefaultMap = new HashMap<>();

    // Preferences
    public static final String PREF_SPECTRUM_ENABLED = "spectrum_enabled";

    public static boolean isPreferenceEnabled(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, (Boolean) sNodeDefaultMap.get(key));
    }

    public static String getPreferenceString(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, (String) sNodeDefaultMap.get(key));
    }
}
