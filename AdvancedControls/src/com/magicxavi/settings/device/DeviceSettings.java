package com.magicxavi.settings.device;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import com.magicxavi.settings.device.kcal.KCalSettingsActivity;
import com.magicxavi.settings.device.preferences.SecureSettingCustomSeekBarPreference;
import com.magicxavi.settings.device.preferences.SecureSettingListPreference;
import com.magicxavi.settings.device.preferences.SecureSettingSwitchPreference;
import com.magicxavi.settings.device.preferences.VibrationSeekBarPreference;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    // Fingerprint options
    //private static final String CATEGORY_FINGERPRINT_OPTIONS = "fp_options";
    //public static final String PREF_FPWAKEUP = "fpwakeup";
    //public static final String FPWAKEUP_PATH = "/sys/devices/soc.0/fpc_fpc1020.110/wakeup_enable";

    // Dirac
    private static final String PREF_ENABLE_DIRAC = "dirac_enabled";
    private static final String PREF_HEADSET = "dirac_headset_pref";
    private static final String PREF_PRESET = "dirac_preset_pref";

    // Gestures
    private static final String CATEGORY_GESTURES= "gestures";
    //public static final String PREF_DT2W = "dt2w";
    //public static final String DT2W_PATH = "/sys/android_touch/doubletap2wake";

    private static final String CATEGORY_DISPLAY = "display";
    private static final String PREF_DEVICE_KCAL = "device_kcal";

    // USB Fastcharge
    private static final String CATEGORY_USB= "usb";
    public static final String PREF_USB_FASTCHARGE = "usbfastcharge";
    public static final String USB_FASTCHARGE_PATH = "/sys/kernel/fast_charge/force_fast_charge";

    // YELLOW LED
    public static final String PREF_TORCH_BRIGHTNESS_2 = "yellow_led";
    public static final String TORCH_2_BRIGHTNESS_PATH = "/sys/devices/soc.0/qpnp-flash-led-23/leds/led:torch_1/max_brightness";

    // WHITE LED
    public static final String PREF_TORCH_BRIGHTNESS_1 = "white_led";
    public static final String TORCH_1_BRIGHTNESS_PATH = "/sys/devices/soc.0/qpnp-flash-led-23/leds/led:torch_0/max_brightness";

    // Haptic feedback
    public static final String PREF_VIBRATION_STRENGTH = "vibration_strength";
    public static final String VIBRATION_STRENGTH_PATH = "/sys/devices/virtual/timed_output/vibrator/vtg_level";
    public static final int MIN_VIBRATION = 116;
    public static final int MAX_VIBRATION = 3596;

    // Audio
    public static final  String PREF_HEADPHONE_GAIN = "headphone_gain";
    public static final  String PREF_MICROPHONE_GAIN = "microphone_gain";
    public static final  String HEADPHONE_GAIN_PATH = "/sys/kernel/sound_control/headphone_gain";
    public static final  String MICROPHONE_GAIN_PATH = "/sys/kernel/sound_control/mic_gain";

    // QC limit
    //private static final String CATEGORY_QC= "qc";
    //public static final String PREF_QC_LIMIT = "qc_limit";
    //public static final String QC_LIMIT_PATH = "/sys/devices/soc.0/qpnp-smbcharger-16/power_supply/battery/constant_charge_current_max";
    
    // Audio
    public static final  String PREF_BUTTONS_BRIGHTNESS_GAIN = "buttons_brightness_gain";
    public static final  String BUTTONS_BRIGHTNESS_GAIN_PATH = "/sys/class/leds/button-backlight/max_brightness";

    private SecureSettingCustomSeekBarPreference mHeadphoneGain;
    private SecureSettingCustomSeekBarPreference mMicrophoneGain;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_advanced_controls, rootKey);

        String device = FileUtils.getStringProp("ro.build.product", "unknown");

        SecureSettingCustomSeekBarPreference TorchBrightness1 = (SecureSettingCustomSeekBarPreference) findPreference(PREF_TORCH_BRIGHTNESS_1);
        TorchBrightness1.setEnabled(FileUtils.fileWritable(TORCH_1_BRIGHTNESS_PATH));
        TorchBrightness1.setOnPreferenceChangeListener(this);

        SecureSettingCustomSeekBarPreference TorchBrightness2 = (SecureSettingCustomSeekBarPreference) findPreference(PREF_TORCH_BRIGHTNESS_2);
        TorchBrightness2.setEnabled(FileUtils.fileWritable(TORCH_2_BRIGHTNESS_PATH));
        TorchBrightness2.setOnPreferenceChangeListener(this);
        
        SecureSettingCustomSeekBarPreference ButtonsBrightnessGain = (SecureSettingCustomSeekBarPreference) findPreference(PREF_BUTTONS_BRIGHTNESS_GAIN);
        ButtonsBrightnessGain.setEnabled(FileUtils.fileWritable(BUTTONS_BRIGHTNESS_GAIN_PATH));
        ButtonsBrightnessGain.setOnPreferenceChangeListener(this);

        mHeadphoneGain = (SecureSettingCustomSeekBarPreference) findPreference(PREF_HEADPHONE_GAIN);
        mHeadphoneGain.setOnPreferenceChangeListener(this);

        mMicrophoneGain = (SecureSettingCustomSeekBarPreference) findPreference(PREF_MICROPHONE_GAIN);
        mMicrophoneGain.setOnPreferenceChangeListener(this);

        //if (FileUtils.fileWritable(QC_LIMIT_PATH)) {
        //SecureSettingCustomSeekBarPreference QuickCharge = (SecureSettingCustomSeekBarPreference) findPreference(PREF_QC_LIMIT);
        //QuickCharge.setEnabled(FileUtils.fileWritable(QC_LIMIT_PATH));
        //QuickCharge.setOnPreferenceChangeListener(this);
        //} else {
        //    getPreferenceScreen().removePreference(findPreference(CATEGORY_QC));
        //}

        VibrationSeekBarPreference vibrationStrength = (VibrationSeekBarPreference) findPreference(PREF_VIBRATION_STRENGTH);
        vibrationStrength.setEnabled(FileUtils.fileWritable(VIBRATION_STRENGTH_PATH));
        vibrationStrength.setOnPreferenceChangeListener(this);

        Preference kcal = findPreference(PREF_DEVICE_KCAL);

        kcal.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), KCalSettingsActivity.class);
            startActivity(intent);
            return true;
        });

        boolean enhancerEnabled;
        try {
            enhancerEnabled = DiracService.sDiracUtils.isDiracEnabled();
        } catch (java.lang.NullPointerException e) {
            getContext().startService(new Intent(getContext(), DiracService.class));
            try {
                enhancerEnabled = DiracService.sDiracUtils.isDiracEnabled();
            } catch (NullPointerException ne) {
                // Avoid crash
                ne.printStackTrace();
                enhancerEnabled = false;
            }
        }

        SecureSettingSwitchPreference enableDirac = (SecureSettingSwitchPreference) findPreference(PREF_ENABLE_DIRAC);
        enableDirac.setOnPreferenceChangeListener(this);
        enableDirac.setChecked(enhancerEnabled);

        SecureSettingListPreference headsetType = (SecureSettingListPreference) findPreference(PREF_HEADSET);
        headsetType.setOnPreferenceChangeListener(this);

        SecureSettingListPreference preset = (SecureSettingListPreference) findPreference(PREF_PRESET);
        preset.setOnPreferenceChangeListener(this);


        //if (FileUtils.fileWritable(FPWAKEUP_PATH)) {
        //    SecureSettingSwitchPreference fpwakeup = (SecureSettingSwitchPreference) findPreference(PREF_FPWAKEUP);
        //    fpwakeup.setChecked(FileUtils.getFileValueAsBoolean(FPWAKEUP_PATH, false));
        //    fpwakeup.setOnPreferenceChangeListener(this);
        //} else {
        //    getPreferenceScreen().removePreference(findPreference(CATEGORY_FINGERPRINT_OPTIONS));
        //}

        //if (FileUtils.fileWritable(DT2W_PATH)) {
        //    SecureSettingSwitchPreference dt2w = (SecureSettingSwitchPreference) findPreference(PREF_DT2W);
        //    dt2w.setChecked(FileUtils.getFileValueAsBoolean(DT2W_PATH, false));
        //    dt2w.setOnPreferenceChangeListener(this);
        //} else {
        //    getPreferenceScreen().removePreference(findPreference(CATEGORY_GESTURES));
        //}

        if (FileUtils.fileWritable(USB_FASTCHARGE_PATH)) {
            SecureSettingSwitchPreference usbfastcharge = (SecureSettingSwitchPreference) findPreference(PREF_USB_FASTCHARGE);
            usbfastcharge.setChecked(FileUtils.getFileValueAsBoolean(USB_FASTCHARGE_PATH, false));
            usbfastcharge.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(findPreference(CATEGORY_USB));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        switch (key) {
            case PREF_TORCH_BRIGHTNESS_2:
                FileUtils.setValue(TORCH_2_BRIGHTNESS_PATH, (int) value);
                break;

            case PREF_TORCH_BRIGHTNESS_1:
                FileUtils.setValue(TORCH_1_BRIGHTNESS_PATH, (int) value);
                break;
                
            case PREF_BUTTONS_BRIGHTNESS_GAIN:
                FileUtils.setValue(BUTTONS_BRIGHTNESS_GAIN_PATH, (int) value);
                break;

            case PREF_VIBRATION_STRENGTH:
                double vibrationValue = (int) value / 100.0 * (MAX_VIBRATION - MIN_VIBRATION) + MIN_VIBRATION;
                FileUtils.setValue(VIBRATION_STRENGTH_PATH, vibrationValue);
                break;

            case PREF_ENABLE_DIRAC:
                try {
                    DiracService.sDiracUtils.setEnabled((boolean) value);
                } catch (java.lang.NullPointerException e) {
                    getContext().startService(new Intent(getContext(), DiracService.class));
                    DiracService.sDiracUtils.setEnabled((boolean) value);
                }
                break;

            case PREF_HEADSET:
                try {
                    DiracService.sDiracUtils.setHeadsetType(Integer.parseInt(value.toString()));
                } catch (java.lang.NullPointerException e) {
                    getContext().startService(new Intent(getContext(), DiracService.class));
                    DiracService.sDiracUtils.setHeadsetType(Integer.parseInt(value.toString()));
                }
                break;

            case PREF_PRESET:
                try {
                    DiracService.sDiracUtils.setLevel(String.valueOf(value));
                } catch (java.lang.NullPointerException e) {
                    getContext().startService(new Intent(getContext(), DiracService.class));
                    DiracService.sDiracUtils.setLevel(String.valueOf(value));
                }
                break;

            //case PREF_FPWAKEUP:
            //    FileUtils.setValue(FPWAKEUP_PATH, (boolean) value);
            //    break;

            //case PREF_DT2W:
            //    FileUtils.setValue(DT2W_PATH, (boolean) value);
            //    break;

            case PREF_USB_FASTCHARGE:
                FileUtils.setValue(USB_FASTCHARGE_PATH, (boolean) value);
                break;

            case PREF_HEADPHONE_GAIN:
                FileUtils.setValue(HEADPHONE_GAIN_PATH, value + " " + value);
                break;

            case PREF_MICROPHONE_GAIN:
                FileUtils.setValue(MICROPHONE_GAIN_PATH, (int) value);
                break;

            //case PREF_QC_LIMIT:
            //    double quickchargeValue = (int) value * 1000.0;
            //    FileUtils.setValue(QC_LIMIT_PATH, quickchargeValue);
            //    break;

            default:
                break;
        }
        return true;
    }

    private boolean isAppNotInstalled(String uri) {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }
}
