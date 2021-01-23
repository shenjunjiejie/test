package com.lirongyuns.happyrupees.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyBankJson extends HttpBean {
    
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
        private int memberId;
        private String beneAccno;
        private String bankName;
        private String beneIfsc;
        private String termValidity;
        private String accountId;
        
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
        
        public int getMemberId() {
            return memberId;
        }
        
        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }
        
        public String getBeneAccno() {
            return beneAccno;
        }
        
        public void setBeneAccno(String beneAccno) {
            this.beneAccno = beneAccno;
        }
        
        public String getBankName() {
            return bankName;
        }
        
        public void setBankName(String bankName) {
            this.bankName = bankName;
        }
        
        public String getBeneIfsc() {
            return beneIfsc;
        }
        
        public void setBeneIfsc(String beneIfsc) {
            this.beneIfsc = beneIfsc;
        }
        
        public String getTermValidity() {
            return termValidity;
        }
        
        public void setTermValidity(String termValidity) {
            this.termValidity = termValidity;
        }
        
        public String getAccountId() {
            return accountId;
        }
        
        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
        
        public static class ParamsBean {
        }
    }
}
