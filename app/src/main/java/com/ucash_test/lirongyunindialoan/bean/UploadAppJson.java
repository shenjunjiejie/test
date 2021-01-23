package com.ucash_test.lirongyunindialoan.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UploadAppJson extends HttpBean {
    
    
    /**
     * data : {"link":"http://147.139.1.49/apk/_1.apk","update":"1","version":"1"}
     */
    
    private DataBean data;
    
    public DataBean getData() {
        return data;
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
    
    public void setData(DataBean data) {
        this.data = data;
    }
    
    public static class DataBean {
        /**
         * link : http://147.139.1.49/apk/lry_1.apk
         * update : 1
         * version : 1
         */
        
        private String link;
        private String update;
        private String version;
        
        public String getLink() {
            return link;
        }
        
        public void setLink(String link) {
            this.link = link;
        }
        
        public String getUpdate() {
            return update;
        }
        
        public void setUpdate(String update) {
            this.update = update;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
    }
}
