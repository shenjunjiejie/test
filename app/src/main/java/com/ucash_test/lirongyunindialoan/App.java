package com.ucash_test.lirongyunindialoan;

import android.app.Application;

import com.ucash_test.lirongyunindialoan.bean.MemberInfo;
import com.ucash_test.lirongyunindialoan.myosotisutils.DataSPUtils;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        mDataSPUtils = new DataSPUtils(this);//添加sharepreference 工具
    }
    /* sharepreference 工具 */
    DataSPUtils mDataSPUtils;
    MemberInfo mMemberInfo;

    /* sharepreference 工具 */

    public void setRiskId(String RiskId) {

        mDataSPUtils.put("RiskId", RiskId);
    }

    public String getRiskId() {
        return mDataSPUtils.getString("RiskId");
    }

    /* OCR技术中人脸识别的数据记录 */
//    @Override
//    public void setResult(DFProductResult result) {
//        this.mResult = result;
//    }
//
//    @Override
//    public DFProductResult getResult() {
//        return mResult;
//    }
    /* OCR技术中人脸识别的数据记录 */

    public MemberInfo.Info getMemberInfo() {
        return mMemberInfo.getInfo();
    }

    public String getPhoneNumber() {
        return mDataSPUtils.getString("phoneNumber");
    }

    public void setPhoneNumber(String phoneNumber) {
        mDataSPUtils.put("phoneNumber", phoneNumber);
    }

    public String getMemberId() {
        return mDataSPUtils.getString("memberId");
    }

    public void setMemberId(String memberId) {
        mDataSPUtils.put("memberId", memberId);
    }

    public boolean isLogin() {
        /**
         * 测试行
         */
        /*setMemberId("292");
        setPhoneNumber("13662567125");*/
        return mDataSPUtils.have("memberId");
    }

    public void clearData() {
        mDataSPUtils.clear();
    }


    public String getPhoneNum() {
        return mDataSPUtils.getString(DataSPUtils.LOGIN_PHONE_NUM_KEY);
    }

    public void setPhoneNum(String phoneNumber) {
        mDataSPUtils.put(DataSPUtils.LOGIN_PHONE_NUM_KEY, phoneNumber);
    }

    public Boolean getLoginStatus() {
        return mDataSPUtils.getBoolean(DataSPUtils.LOGIN_STATUS);
    }

    public void setLoginStatus(Boolean phoneNumber) {
        mDataSPUtils.put(DataSPUtils.LOGIN_STATUS, phoneNumber);
    }


    public void setUserToken(String token){
//        mDataSPUtils.put();
    }
}
