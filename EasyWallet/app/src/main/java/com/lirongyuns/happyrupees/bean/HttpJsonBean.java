package com.lirongyuns.happyrupees.bean;

import org.json.JSONObject;

/**
 * json bean
 */
public class HttpJsonBean extends HttpBean
{
    private JSONObject mJSONObject;

    public JSONObject getJSONObject() {
        return mJSONObject;
    }

    public void setJSONObject(JSONObject JSONObject) {
        mJSONObject = JSONObject;
    }
}
