package com.lge.settings.device.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

public class PreferenceHelper {

    /**
     * Get status of spectrum profiles
     * @param context - ¯\_(ツ)_/¯
     * @return - Spectrum enabled status
     */
    public static boolean isSpectrumEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(Constants.PREF_SPECTRUM_ENABLED, true);
    }

    /**
     * Save spectrum profiles status
     * @param context - ¯\_(ツ)_/¯
     * @param value - nickname
     */
    public static void setSpectrumEnabled(Context context, boolean value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(Constants.PREF_SPECTRUM_ENABLED, value).apply();
    }
}
