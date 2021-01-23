package com.lirongyuns.happyrupees.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具
 * @author Krear 2018/8/16
 */
public class SharePreferenceUtil
{
    public static final String USER_DATA = "user_data";

    /**
     * 存储登录电话号码的键
     */
    public static final String LOGIN_PHONE_NUM_KEY="phoneNum";
    /**
     * 存储登录状态的键
     */
    public static final String LOGIN_STATUS="loginStatus";

    private SharedPreferences sp;

    public SharePreferenceUtil(Context context)
    {
        this(context, USER_DATA);
    }

    public SharePreferenceUtil(Context context, String name)
    {
        this(context, name, Context.MODE_PRIVATE);
    }

    public SharePreferenceUtil(Context context, String name, int mode)
    {
        sp = context.getSharedPreferences(name, mode);
    }

    /**
     * putString
     * @param key
     * @param value
     */
    public void put(String key, String value)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key)
    {
        return sp.getString(key, "");
    }

    /**
     * putString
     * @param key
     * @param value
     */
    public void put(String key, int value)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key)
    {
        return sp.getInt(key, -1);
    }

    /**
     * putString
     * @param key
     * @param value
     */
    public void put(String key, boolean value)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key)
    {
        return sp.getBoolean(key, false);
    }

    public boolean have(String key) {
        return sp.contains(key);
    }

    /**
     * 清空数据
     */
    public void clear()
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


}
