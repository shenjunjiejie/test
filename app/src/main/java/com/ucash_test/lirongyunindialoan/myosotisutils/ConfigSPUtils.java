package com.ucash_test.lirongyunindialoan.myosotisutils;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigSPUtils {
    private static String tag = ConfigSPUtils.class.getSimpleName();
    private static final String SP_NAME = "config";
    private static SharedPreferences sp;
    private static Context context;

    public ConfigSPUtils() {
    }

    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", 0);
        }

        return sp;
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        getSp(context).edit().putBoolean(key, value).commit();
    }

    public static void saveString(Context context, String key, String value) {
        getSp(context).edit().putString(key, value).commit();
    }

    public static void saveLong(Context context, String key, long value) {
        getSp(context).edit().putLong(key, value).commit();
    }

    public static void saveInt(Context context, String key, int value) {
        getSp(context).edit().putInt(key, value).commit();
    }

    public static void saveFloat(Context context, String key, float value) {
        getSp(context).edit().putFloat(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return getSp(context).getString(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSp(context).getInt(key, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSp(context).getLong(key, defValue);
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getSp(context).getFloat(key, defValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSp(context).getBoolean(key, defValue);
    }

    public static void init(Context initContext) {
        if (sp == null) {
            context = initContext;
            sp = context.getSharedPreferences("config", 0);
        }

    }

    public static int getShareIntData(String key) {
        return sp.getInt(key, 0);
    }

    public static void setShareIntData(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public static String getShareStringData(String key) {
        return sp.getString(key, "tag");
    }

    public static void setShareStringData(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public static boolean getShareBooleanData(String key) {
        return sp.getBoolean(key, false);
    }

    public static void setShareBooleanData(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public static float getShareFloatData(String key) {
        return sp.getFloat(key, 0.0F);
    }

    public static void setShareFloatData(String key, float value) {
        sp.edit().putFloat(key, value).commit();
    }

    public static long getShareLongData(String key) {
        return sp.getLong(key, 0L);
    }

    public static void setShareLongData(String key, long value) {
        sp.edit().putLong(key, value).commit();
    }

    public static void clearShareData() {
        String data = getString(context, "registerData", "");
        boolean is_professional_chooosed = getBoolean(context, "is_professional_chooosed", false);
        boolean isFirstLaunched = getBoolean(context, "isFirstLaunched", true);
        String loginUserName = getString(context, "loginUserName", "");
        int original_answered_paper_count = getInt(context, "answered_paper_count", 0);
        int default_person_avatar_position = getInt(context, "default_person_avatar_position", 0);
        String user_order_live_course_phone = getString(context, "user_order_live_course_phone", "");
        boolean isClosePage = getBoolean(context, "isClosePage", false);
        String device_id = getString(context, "device_id", "");
        sp.edit().clear().commit();
        saveString(context, "registerData", data);
        saveBoolean(context, "is_professional_chooosed", is_professional_chooosed);
        saveBoolean(context, "isFirstLaunched", isFirstLaunched);
        saveString(context, "loginUserName", loginUserName);
        saveInt(context, "answered_paper_count", original_answered_paper_count);
        saveInt(context, "default_person_avatar_position", default_person_avatar_position);
        saveString(context, "user_order_live_course_phone", user_order_live_course_phone);
        saveBoolean(context, "isClosePage", isClosePage);
        saveString(context, "device_id", device_id);
    }

    public static void clearShareDatas(String key) {
        sp.edit().remove(key).commit();
    }
}
