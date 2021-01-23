package com.lirongyuns.happyrupees.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 获取通讯录的工具类
 */
public class PhoneUtils {
    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    //时间
    public final static String TIME =  ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP;

    //联系人提供者的uri
    private static Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    
    /**
     * 获取所有联系人
     * @return ArrayList<JSONObject>
     * @throws JSONException 异常
     */
    public static ArrayList<JSONObject> getContacts(Context context) throws JSONException {

        ArrayList<JSONObject> phoneDatas = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri, new String[]{NUM,NAME,TIME},null,null,null);
        if(null == cursor){
            return null;
        }
        while (cursor.moveToNext()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contacts", cursor.getString(cursor.getColumnIndex(NAME)));
            jsonObject.put("phoneNumber", cursor.getString(cursor.getColumnIndex(NUM)));
            long time = cursor.getLong(cursor.getColumnIndex(TIME));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd HH:mm:ss");
            Date date = new Date(time);
            String dateStr = simpleDateFormat.format(date);
            jsonObject.put("updateTime",dateStr);
            jsonObject.put("createTime",dateStr);
            phoneDatas.add(jsonObject);
        }
        cursor.close();
        return phoneDatas;
    }

}
