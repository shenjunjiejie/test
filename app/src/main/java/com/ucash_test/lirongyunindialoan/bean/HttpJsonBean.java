package com.ucash_test.lirongyunindialoan.bean;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * json bean
 */
public class HttpJsonBean extends HttpBean
{
    private JSONObject mJSONObject;

    public JSONObject getJSONObject() {
        return mJSONObject;
    }

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

    public void setJSONObject(JSONObject JSONObject) {
        mJSONObject = JSONObject;
    }
}
