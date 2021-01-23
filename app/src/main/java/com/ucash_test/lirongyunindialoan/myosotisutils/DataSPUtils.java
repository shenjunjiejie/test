package com.ucash_test.lirongyunindialoan.myosotisutils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * SharedPreferences工具
 * @author prayer 2020/11/19
 */
public class DataSPUtils
{
    public static final String USER_DATA = "user_data";
    public void defeatCoding(){
        //花指令
        BufferedReader br =null;
        try {
            br = new BufferedReader(new FileReader("fakeFile"));
            String line;
            while((line=br.readLine())!= null){
                String[] splited = line.split(" +");
                if(splited.length >= 0){
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                br.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    /**
     * 存储登录电话号码的键
     */
    public static final String LOGIN_PHONE_NUM_KEY="phoneNum";
    /**
     * 存储登录状态的键
     */
    public static final String LOGIN_STATUS="loginStatus";

    private SharedPreferences sp;

    public DataSPUtils(Context context)
    {
        this(context, USER_DATA);
    }

    public DataSPUtils(Context context, String name)
    {
        this(context, name, Context.MODE_PRIVATE);
    }

    public DataSPUtils(Context context, String name, int mode)
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

