package com.lirongyuns.happyrupees.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OCRImageJson extends HttpBean {
    
    private DataBean data;
    
    public DataBean getData() {
        return data;
    }
    
    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String searchValue;
        private String createBy;
        private String createTime;
        private String updateBy;
        private String updateTime;
        private String remark;
        private String dataScope;
        private ParamsBean params;
        private int id;
        private String bizeType;
        private int bizeId;
        private String fileName;
        private String fileSize;
        private String filePath;
        private String fileType;
        private String dr;
        
        public String getSearchValue() {
            return searchValue;
        }
        
        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }
        
        public String getCreateBy() {
            return createBy;
        }
        
        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }
        
        public String getCreateTime() {
            return createTime;
        }
        
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        
        public String getUpdateBy() {
            return updateBy;
        }
        
        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }
        
        public String getUpdateTime() {
            return updateTime;
        }
        
        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
        
        public String getRemark() {
            return remark;
        }
        
        public void setRemark(String remark) {
            this.remark = remark;
        }
        
        public String getDataScope() {
            return dataScope;
        }
        
        public void setDataScope(String dataScope) {
            this.dataScope = dataScope;
        }
        
        public ParamsBean getParams() {
            return params;
        }
        
        public void setParams(ParamsBean params) {
            this.params = params;
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public String getBizeType() {
            return bizeType;
        }
        
        public void setBizeType(String bizeType) {
            this.bizeType = bizeType;
        }
        
        public int getBizeId() {
            return bizeId;
        }
        
        public void setBizeId(int bizeId) {
            this.bizeId = bizeId;
        }
        
        public String getFileName() {
            return fileName;
        }
        
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
        public String getFileSize() {
            return fileSize;
        }
        
        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }
        
        public String getFilePath() {
            return filePath;
        }
        
        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
        
        public String getFileType() {
            return fileType;
        }
        
        public void setFileType(String fileType) {
            this.fileType = fileType;
        }
        
        public String getDr() {
            return dr;
        }
        
        public void setDr(String dr) {
            this.dr = dr;
        }
        
        public static class ParamsBean {
        }
    }
}
