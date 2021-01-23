package com.lirongyuns.happyrupees.bean;

public class UploadAppJson extends HttpBean {

    
    private DataBean data;
    
    public DataBean getData() {
        return data;
    }
    
    public void setData(DataBean data) {
        this.data = data;
    }
    
    public static class DataBean {
        
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
