package com.lirongyuns.happyrupees.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoanDetailsJson extends HttpBean{

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
        private String billNo;
        private String billNoparty;
        private String paymentStatus;
        private String lendingTime;
        private String overdueTime;
        private int memberId;
        private int approvedId;
        private String channelId;
        private int productId;
        private String collectorId;
        private String applicationAmount;
        private String loanAmount;
        private String repaymentAmount;
        private String lateFee;
        private String repayable;
        private String approvalTime;
        private double fraction;
        private String repaymentStatus;
        private String overdue;
        private String collectionStatus;
        private String overdueDays;
        private String refinancing;
        private int numberPeriods;
        private String contractAddress;
        private String agreementAddress;
        private String completionTime;
        private String bankStatement;
        private String subapprovalTime;
        private String reminders;
        private String collectionNotes;
        private String serviceRate;
        private String warrantUrl;
        private String paymentChannels;
        private String phone;
        private String userName;
        private String appName;
        private String colName;
        private String channelName;
        private String times;
        private String bankNo;
        private String gstTax;
        private String interest;
    
        public String getInterest() {
            return interest;
        }
    
        public String getGstTax() {
            return gstTax;
        }
    
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
        
        public String getBillNo() {
            return billNo;
        }
        
        public void setBillNo(String billNo) {
            this.billNo = billNo;
        }
        
        public String getBillNoparty() {
            return billNoparty;
        }
        
        public void setBillNoparty(String billNoparty) {
            this.billNoparty = billNoparty;
        }
        
        public String getPaymentStatus() {
            return paymentStatus;
        }
        
        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }
        
        public String getLendingTime() {
            return lendingTime;
        }
        
        public void setLendingTime(String lendingTime) {
            this.lendingTime = lendingTime;
        }
        
        public String getOverdueTime() {
            return overdueTime;
        }
        
        public void setOverdueTime(String overdueTime) {
            this.overdueTime = overdueTime;
        }
        
        public int getMemberId() {
            return memberId;
        }
        
        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }
        
        public int getApprovedId() {
            return approvedId;
        }
        
        public void setApprovedId(int approvedId) {
            this.approvedId = approvedId;
        }
        
        public String getChannelId() {
            return channelId;
        }
        
        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }
        
        public int getProductId() {
            return productId;
        }
        
        public void setProductId(int productId) {
            this.productId = productId;
        }
        
        public String getCollectorId() {
            return collectorId;
        }
        
        public void setCollectorId(String collectorId) {
            this.collectorId = collectorId;
        }
        
        public String getApplicationAmount() {
            return applicationAmount;
        }
        
        public void setApplicationAmount(String applicationAmount) {
            this.applicationAmount = applicationAmount;
        }
        
        public String getLoanAmount() {
            return loanAmount;
        }
        
        public void setLoanAmount(String loanAmount) {
            this.loanAmount = loanAmount;
        }
        
        public String getRepaymentAmount() {
            return repaymentAmount;
        }
        
        public void setRepaymentAmount(String repaymentAmount) {
            this.repaymentAmount = repaymentAmount;
        }
        
        public String getLateFee() {
            return lateFee;
        }
        
        public void setLateFee(String lateFee) {
            this.lateFee = lateFee;
        }
        
        public String getRepayable() {
            return repayable;
        }
        
        public void setRepayable(String repayable) {
            this.repayable = repayable;
        }
        
        public String getApprovalTime() {
            return approvalTime;
        }
        
        public void setApprovalTime(String approvalTime) {
            this.approvalTime = approvalTime;
        }
        
        public double getFraction() {
            return fraction;
        }
        
        public void setFraction(double fraction) {
            this.fraction = fraction;
        }
        
        public String getRepaymentStatus() {
            return repaymentStatus;
        }
        
        public void setRepaymentStatus(String repaymentStatus) {
            this.repaymentStatus = repaymentStatus;
        }
        
        public String getOverdue() {
            return overdue;
        }
        
        public void setOverdue(String overdue) {
            this.overdue = overdue;
        }
        
        public String getCollectionStatus() {
            return collectionStatus;
        }
        
        public void setCollectionStatus(String collectionStatus) {
            this.collectionStatus = collectionStatus;
        }
        
        public String getOverdueDays() {
            return overdueDays;
        }
        
        public void setOverdueDays(String overdueDays) {
            this.overdueDays = overdueDays;
        }
        
        public String getRefinancing() {
            return refinancing;
        }
        
        public void setRefinancing(String refinancing) {
            this.refinancing = refinancing;
        }
        
        public int getNumberPeriods() {
            return numberPeriods;
        }
        
        public void setNumberPeriods(int numberPeriods) {
            this.numberPeriods = numberPeriods;
        }
        
        public String getContractAddress() {
            return contractAddress;
        }
        
        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }
        
        public String getAgreementAddress() {
            return agreementAddress;
        }
        
        public void setAgreementAddress(String agreementAddress) {
            this.agreementAddress = agreementAddress;
        }
        
        public String getCompletionTime() {
            return completionTime;
        }
        
        public void setCompletionTime(String completionTime) {
            this.completionTime = completionTime;
        }
        
        public String getBankStatement() {
            return bankStatement;
        }
        
        public void setBankStatement(String bankStatement) {
            this.bankStatement = bankStatement;
        }
        
        public String getSubapprovalTime() {
            return subapprovalTime;
        }
        
        public void setSubapprovalTime(String subapprovalTime) {
            this.subapprovalTime = subapprovalTime;
        }
        
        public String getReminders() {
            return reminders;
        }
        
        public void setReminders(String reminders) {
            this.reminders = reminders;
        }
        
        public String getCollectionNotes() {
            return collectionNotes;
        }
        
        public void setCollectionNotes(String collectionNotes) {
            this.collectionNotes = collectionNotes;
        }
        
        public String getServiceRate() {
            return serviceRate;
        }
        
        public void setServiceRate(String serviceRate) {
            this.serviceRate = serviceRate;
        }
        
        public String getWarrantUrl() {
            return warrantUrl;
        }
        
        public void setWarrantUrl(String warrantUrl) {
            this.warrantUrl = warrantUrl;
        }
        
        public String getPaymentChannels() {
            return paymentChannels;
        }
        
        public void setPaymentChannels(String paymentChannels) {
            this.paymentChannels = paymentChannels;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public void setPhone(String phone) {
            this.phone = phone;
        }
        
        public String getUserName() {
            return userName;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getAppName() {
            return appName;
        }
        
        public void setAppName(String appName) {
            this.appName = appName;
        }
        
        public String getColName() {
            return colName;
        }
        
        public void setColName(String colName) {
            this.colName = colName;
        }
        
        public String getChannelName() {
            return channelName;
        }
        
        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }
        
        public String getTimes() {
            return times;
        }
        
        public void setTimes(String times) {
            this.times = times;
        }
        
        public String getBankNo() {
            return bankNo;
        }
        
        public void setBankNo(String bankNo) {
            this.bankNo = bankNo;
        }
        
        public static class ParamsBean {
        }
    }
}
