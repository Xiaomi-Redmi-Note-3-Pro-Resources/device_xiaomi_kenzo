package com.lge.settings.device;

import com.lge.settings.device.utils.Constants;
import com.lge.settings.device.utils.FileUtils;

public class Battery {

    private Battery() {

    }

    public static String getBatteryCycle() {
        return FileUtils.readOneLine(Constants.BATTERY_CYCLE_NODE);
    }

    public static String getBatteryTemp() {
        String temp = FileUtils.readOneLine(Constants.BATTERY_TEMP_NODE);
        return temp.substring(0, temp.length()-1);
    }

    public static String getBatteryHealth() {
        return FileUtils.readOneLine(Constants.BATTERY_HEALTH_NODE);
    }
}
