package com.lirongyuns.happyrupees.internet;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求参数构建者
 * @author Krear on 2017/5/17.
 */
public class JsonCreator
{
    private JSONObject json;
    private String url;

    public JsonCreator()
    {
        json = new JSONObject();
    }

    public JsonCreator(String url) {
        this.url = url;
        json = new JSONObject();
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, Object value)
    {
        try {
            json.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据对象生成JSON
     */
    public void fromObject(Object object)
    {
        Gson gson = new Gson();
        try {
            json = new JSONObject(gson.toJson(object));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return json.length();
    }

    /**
     * 返回JSON字符串
     * @return
     */
    @Override
    public String toString() {
        return json.toString();
    }

    /**
     * 返回JSON对象
     * @return
     */
    public JSONObject toJSONObject() {
        return json;
    }

    public String getUrl() {
        return url;
    }
}
