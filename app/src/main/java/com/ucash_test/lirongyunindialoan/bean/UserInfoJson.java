package com.ucash_test.lirongyunindialoan.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserInfoJson extends HttpBean {

    /**
     * data : {"loanMember":{"searchValue":null,"createBy":null,"createTime":"2020-08-06 11:49:43","updateBy":null,"updateTime":null,"remark":null,"dataScope":null,"params":{},"id":20,"approvalId":null,"collectorId":null,"productId":null,"channelId":null,"birthday":"07/09/1990","mobile":"911867676232","password":null,"realName":"NAGSEN SHIVAJIRAO DHAWALE","panId":"DEXPD3550L","aadhaarId":"840736176820","balance":0,"address":"S/O Shivajil Dhawale, AtMaskl Post Berall, Loha, Maski, Nanded, Maharashtra - 431708","email":null,"status":"1","mpu":null,"applicationTime":null,"loanStatus":"1","creditScore":null,"creditTime":null,"uploadSms":"1","uploadAlbum":"1","uploadAddress":"1","uploadApp":"1","uploadInformation":"1","ocrInspect":"1","liveInspect":"1","faceInspect":"0","cardInspect":"0","refinancing":null,"subapprovalTime":null,"detection":null,"contractAddress":null,"agreementAddress":null,"warrantUrl":null,"contactId":null,"accountId":null,"channelName":null,"approvalName":null,"registrationTime":null,"lastLoginTime":null},"auditStatus":null,"appSeting":"1"}
     */

    private DataBean data;


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
    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * loanMember : {"searchValue":null,"createBy":null,"createTime":"2020-08-06 11:49:43","updateBy":null,"updateTime":null,"remark":null,"dataScope":null,"params":{},"id":20,"approvalId":null,"collectorId":null,"productId":null,"channelId":null,"birthday":"07/09/1990","mobile":"911867676232","password":null,"realName":"NAGSEN SHIVAJIRAO DHAWALE","panId":"DEXPD3550L","aadhaarId":"840736176820","balance":0,"address":"S/O Shivajil Dhawale, AtMaskl Post Berall, Loha, Maski, Nanded, Maharashtra - 431708","email":null,"status":"1","mpu":null,"applicationTime":null,"loanStatus":"1","creditScore":null,"creditTime":null,"uploadSms":"1","uploadAlbum":"1","uploadAddress":"1","uploadApp":"1","uploadInformation":"1","ocrInspect":"1","liveInspect":"1","faceInspect":"0","cardInspect":"0","refinancing":null,"subapprovalTime":null,"detection":null,"contractAddress":null,"agreementAddress":null,"warrantUrl":null,"contactId":null,"accountId":null,"channelName":null,"approvalName":null,"registrationTime":null,"lastLoginTime":null}
         * auditStatus : null
         * appSeting : 1
         */

        private LoanMemberBean loanMember;
        private String auditStatus;
        private String appSeting;//AB面 设置 0:A面 1:B面
        private String uploadSuccessful;//
        private String ocrType;// 0:Advance  1:Accuauth
        private String cardCheckingMsg;//绑卡失败原因

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        private String payType; //0：关闭 1：Mpurse 2：Razorpay 3.cashfree',

        public String getOcrType() {
            return ocrType;
        }

        public String getCardCheckingMsg() {
            return cardCheckingMsg;
        }

        public String getUploadSuccessful() {
            return uploadSuccessful;
        }

        public void setUploadSuccessful(String uploadSuccessful) {
            this.uploadSuccessful = uploadSuccessful;
        }

        public LoanMemberBean getLoanMember() {
            return loanMember;
        }

        public void setLoanMember(LoanMemberBean loanMember) {
            this.loanMember = loanMember;
        }

        public String getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
        }

        public String getAppSeting() {
            return appSeting;
        }

        public void setAppSeting(String appSeting) {
            this.appSeting = appSeting;
        }

        public static class LoanMemberBean {
            /**
             * searchValue : null
             * createBy : null
             * createTime : 2020-08-06 11:49:43
             * updateBy : null
             * updateTime : null
             * remark : null
             * dataScope : null
             * params : {}
             * id : 20
             * approvalId : null
             * collectorId : null
             * productId : null
             * channelId : null
             * birthday : 07/09/1990
             * mobile : 911867676232
             * password : null
             * realName : NAGSEN SHIVAJIRAO DHAWALE
             * panId : DEXPD3550L
             * aadhaarId : 840736176820
             * balance : 0.0
             * address : S/O Shivajil Dhawale, AtMaskl Post Berall, Loha, Maski, Nanded, Maharashtra - 431708
             * email : null
             * status : 1
             * mpu : null
             * applicationTime : null
             * loanStatus : 1
             * creditScore : null
             * creditTime : null
             * uploadSms : 1
             * uploadAlbum : 1
             * uploadAddress : 1
             * uploadApp : 1
             * uploadInformation : 1
             * ocrInspect : 1
             * liveInspect : 1
             * faceInspect : 0
             * cardInspect : 0
             * refinancing : null
             * subapprovalTime : null
             * detection : null
             * contractAddress : null
             * agreementAddress : null
             * warrantUrl : null
             * contactId : null
             * accountId : null
             * channelName : null
             * approvalName : null
             * registrationTime : null
             * lastLoginTime : null
             */

            private String searchValue;
            private String createBy;
            private String createTime;
            private String updateBy;
            private String updateTime;
            private String remark;
            private String dataScope;
            private ParamsBean params;
            private int id;
            private String approvalId;
            private String collectorId;
            private String productId;
            private String channelId;
            private String birthday;
            private String mobile;
            private String password;
            private String realName;
            private String panId;
            private String aadhaarId;
            private double balance;
            private String address;
            private String email;
            private String status;
            private String mpu;
            private String applicationTime;
            //借款状态0.注册、1.填写资料中、2.审核中、3.拒绝、4.机审通过、5.机审拒绝、6.人审通过、
            // * 7.人审拒绝 8借款中、9.逾期、10.还款完成 11放款失败、12待还款 13.验证银行卡中
            // 14验证银行卡失败 15验证银行卡成功
            private String loanStatus;
            private String creditScore;
            private String creditTime;
            private String uploadSms;//上传短信 0 未上传 1已上传
            private String uploadAlbum;//上传相册 0 未上传 1已上传
            private String uploadAddress;//	上传通讯录 0 未上传 1已上传
            private String uploadApp;//	上传APP 0 未上传 1已上传
            private String uploadInformation;//上传基本信息 0 未上传 1已上传
            private String ocrInspect;//检查orc 0:未通过 1：已通过
            private String liveInspect;//检查活体 0：未通过 1：已通过
            private String faceInspect;//	检查人脸 0：未通过 1：已通过
            private String cardInspect;//检查AP卡 0：未通过 1：已通过
            private String refinancing;
            private String subapprovalTime;
            private String detection;
            private String contractAddress;
            private String agreementAddress;
            private String warrantUrl;
            private String contactId;
            private String accountId;
            private String channelName;
            private String approvalName;
            private String registrationTime;
            private String lastLoginTime;
            private String resetState;

            public String getResetState() {
                return resetState;
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

            public String getApprovalId() {
                return approvalId;
            }

            public void setApprovalId(String approvalId) {
                this.approvalId = approvalId;
            }

            public String getCollectorId() {
                return collectorId;
            }

            public void setCollectorId(String collectorId) {
                this.collectorId = collectorId;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getChannelId() {
                return channelId;
            }

            public void setChannelId(String channelId) {
                this.channelId = channelId;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public String getPanId() {
                return panId;
            }

            public void setPanId(String panId) {
                this.panId = panId;
            }

            public String getAadhaarId() {
                return aadhaarId;
            }

            public void setAadhaarId(String aadhaarId) {
                this.aadhaarId = aadhaarId;
            }

            public double getBalance() {
                return balance;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMpu() {
                return mpu;
            }

            public void setMpu(String mpu) {
                this.mpu = mpu;
            }

            public String getApplicationTime() {
                return applicationTime;
            }

            public void setApplicationTime(String applicationTime) {
                this.applicationTime = applicationTime;
            }

            public String getLoanStatus() {
                return loanStatus;
            }

            public void setLoanStatus(String loanStatus) {
                this.loanStatus = loanStatus;
            }

            public String getCreditScore() {
                return creditScore;
            }

            public void setCreditScore(String creditScore) {
                this.creditScore = creditScore;
            }

            public String getCreditTime() {
                return creditTime;
            }

            public void setCreditTime(String creditTime) {
                this.creditTime = creditTime;
            }

            public String getUploadSms() {
                return uploadSms;
            }

            public void setUploadSms(String uploadSms) {
                this.uploadSms = uploadSms;
            }

            public String getUploadAlbum() {
                return uploadAlbum;
            }

            public void setUploadAlbum(String uploadAlbum) {
                this.uploadAlbum = uploadAlbum;
            }

            public String getUploadAddress() {
                return uploadAddress;
            }

            public void setUploadAddress(String uploadAddress) {
                this.uploadAddress = uploadAddress;
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

            public String getOcrInspect() {
                return ocrInspect;
            }

            public void setOcrInspect(String ocrInspect) {
                this.ocrInspect = ocrInspect;
            }

            public String getLiveInspect() {
                return liveInspect;
            }

            public void setLiveInspect(String liveInspect) {
                this.liveInspect = liveInspect;
            }

            public String getFaceInspect() {
                return faceInspect;
            }

            public void setFaceInspect(String faceInspect) {
                this.faceInspect = faceInspect;
            }

            public String getCardInspect() {
                return cardInspect;
            }

            public void setCardInspect(String cardInspect) {
                this.cardInspect = cardInspect;
            }

            public String getRefinancing() {
                return refinancing;
            }

            public void setRefinancing(String refinancing) {
                this.refinancing = refinancing;
            }

            public String getSubapprovalTime() {
                return subapprovalTime;
            }

            public void setSubapprovalTime(String subapprovalTime) {
                this.subapprovalTime = subapprovalTime;
            }

            public String getDetection() {
                return detection;
            }

            public void setDetection(String detection) {
                this.detection = detection;
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

            public String getWarrantUrl() {
                return warrantUrl;
            }

            public void setWarrantUrl(String warrantUrl) {
                this.warrantUrl = warrantUrl;
            }

            public String getContactId() {
                return contactId;
            }

            public void setContactId(String contactId) {
                this.contactId = contactId;
            }

            public String getAccountId() {
                return accountId;
            }

            public void setAccountId(String accountId) {
                this.accountId = accountId;
            }

            public String getChannelName() {
                return channelName;
            }

            public void setChannelName(String channelName) {
                this.channelName = channelName;
            }

            public String getApprovalName() {
                return approvalName;
            }

            public void setApprovalName(String approvalName) {
                this.approvalName = approvalName;
            }

            public String getRegistrationTime() {
                return registrationTime;
            }

            public void setRegistrationTime(String registrationTime) {
                this.registrationTime = registrationTime;
            }

            public String getLastLoginTime() {
                return lastLoginTime;
            }

            public void setLastLoginTime(String lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
            }

            public static class ParamsBean {
            }
        }
    }
}
