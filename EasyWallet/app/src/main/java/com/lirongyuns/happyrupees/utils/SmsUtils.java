package com.lirongyuns.happyrupees.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 获取短信工具类
 */
public class SmsUtils {

    private static Uri SMS_INBOX = Uri.parse("content://sms/");
    
    /**
     * 获取手机短信
     * @param context 上下文
     * @return ArrayList<HashMap<String, String>>
     */
    public static ArrayList<JSONObject> getPhoneSmsMessage(@NotNull Context context) {
        ArrayList<JSONObject> list = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        int i = 0;
        
        if (null == cur) {
            return list;
        }
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//发件人手机号
//            String name = cur.getString(cur.getColumnIndex("person"));//发件人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            int type = cur.getInt(cur.getColumnIndex("type"));//短信内容
            //1 发件 2收件
            
            if(!TextUtils.isEmpty(body)){
                body = body.replaceAll("\"","\'");
            }
            long date = cur.getLong(cur.getColumnIndex("date"));//短信时间
            JSONObject jsonObject =  new JSONObject();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd HH:mm:ss");
            Date date1 = new Date(date);
            String time = simpleDateFormat.format(date1);
//            String time = secondToDate(date, "yyyy/MM/dd HH:mm:ss");
            try {
                jsonObject.put("name", number);
                jsonObject.put("content", body);
                jsonObject.put("time", time);
                jsonObject.put("type", type);
                list.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return list;
            }
//            i++;
        }
        cur.close();
        return list;
    }
    

/**
 * sms主要结构：
 *  _id：短信序号，如100
 *  thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
 *  address：发件人地址，即手机号，如+8613811810000
 *  person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
 *  date：日期，long型，如1256539465022，可以对日期显示格式进行设置
 *  protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信
 *  read：是否阅读0未读，1已读
 *  status：短信状态-1接收，0complete,64pending,128failed
 *  type：短信类型1是接收到的，2是已发出
 *  body：短信具体内容
 *  service_center：短信服务中心号码编号，如+8613800755500
 */

}
