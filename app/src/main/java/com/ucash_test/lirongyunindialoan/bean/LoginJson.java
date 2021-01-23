package com.ucash_test.lirongyunindialoan.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginJson extends HttpBean{
    
    /**
     * data : {"loanMember":{"balance":0,"cardInspect":"0","createTime":"2020-07-30 15:53:03","faceInspect":"0","id":16,"liveInspect":"0","loanStatus":"0","mobile":"911887676723","ocrInspect":"0","params":{},"status":"1","uploadAddress":"0","uploadAlbum":"0","uploadApp":"0","uploadInformation":"0","uploadSms":"0"}}
     */
    
    private DataBean data;
    
    public DataBean getData() {
        return data;
    }
    
    public void setData(DataBean data) {
        this.data = data;
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
    
    public static class DataBean {
        /**
         * loanMember : {"balance":0,"cardInspect":"0","createTime":"2020-07-30 15:53:03","faceInspect":"0","id":16,"liveInspect":"0","loanStatus":"0","mobile":"911887676723","ocrInspect":"0","params":{},"status":"1","uploadAddress":"0","uploadAlbum":"0","uploadApp":"0","uploadInformation":"0","uploadSms":"0"}
         */
        
        private LoanMemberBean loanMember;
        
        public LoanMemberBean getLoanMember() {
            return loanMember;
        }
        
        public void setLoanMember(LoanMemberBean loanMember) {
            this.loanMember = loanMember;
        }
        
        public static class LoanMemberBean {
            /**
             * balance : 0
             * cardInspect : 0
             * createTime : 2020-07-30 15:53:03
             * faceInspect : 0
             * id : 16
             * liveInspect : 0
             * loanStatus : 0
             * mobile : 911887676723
             * ocrInspect : 0
             * params : {}
             * status : 1
             * uploadAddress : 0
             * uploadAlbum : 0
             * uploadApp : 0
             * uploadInformation : 0
             * uploadSms : 0
             */
            
            private int balance;
            private String cardInspect;
            private String createTime;
            private String faceInspect;
            private int id;
            private String liveInspect;
            private String loanStatus;
            private String mobile;
            private String ocrInspect;
            private ParamsBean params;
            private String status;
            private String uploadAddress;
            private String uploadAlbum;
            private String uploadApp;
            private String uploadInformation;
            private String uploadSms;
            
            public int getBalance() {
                return balance;
            }
            
            public void setBalance(int balance) {
                this.balance = balance;
            }
            
            public String getCardInspect() {
                return cardInspect;
            }
            
            public void setCardInspect(String cardInspect) {
                this.cardInspect = cardInspect;
            }
            
            public String getCreateTime() {
                return createTime;
            }
            
            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
            
            public String getFaceInspect() {
                return faceInspect;
            }
            
            public void setFaceInspect(String faceInspect) {
                this.faceInspect = faceInspect;
            }
            
            public int getId() {
                return id;
            }
            
            public void setId(int id) {
                this.id = id;
            }
            
            public String getLiveInspect() {
                return liveInspect;
            }
            
            public void setLiveInspect(String liveInspect) {
                this.liveInspect = liveInspect;
            }
            
            public String getLoanStatus() {
                return loanStatus;
            }
            
            public void setLoanStatus(String loanStatus) {
                this.loanStatus = loanStatus;
            }
            
            public String getMobile() {
                return mobile;
            }
            
            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
            
            public String getOcrInspect() {
                return ocrInspect;
            }
            
            public void setOcrInspect(String ocrInspect) {
                this.ocrInspect = ocrInspect;
            }
            
            public ParamsBean getParams() {
                return params;
            }
            
            public void setParams(ParamsBean params) {
                this.params = params;
            }
            
            public String getStatus() {
                return status;
            }
            
            public void setStatus(String status) {
                this.status = status;
            }
            
            public String getUploadAddress() {
                return uploadAddress;
            }
            
            public void setUploadAddress(String uploadAddress) {
                this.uploadAddress = uploadAddress;
            }
            
            public String getUploadAlbum() {
                return uploadAlbum;
            }
            
            public void setUploadAlbum(String uploadAlbum) {
                this.uploadAlbum = uploadAlbum;
            }
            
            public String getUploadApp() {
                return uploadApp;
            }
            
            public void setUploadApp(String uploadApp) {
                this.uploadApp = uploadApp;
            }
            
            public String getUploadInformation() {
                return uploadInformation;
            }
            
            public void setUploadInformation(String uploadInformation) {
                this.uploadInformation = uploadInformation;
            }
            
            public String getUploadSms() {
                return uploadSms;
            }
            
            public void setUploadSms(String uploadSms) {
                this.uploadSms = uploadSms;
            }
            
            public static class ParamsBean {
            }
        }
    }
}
