package com.lirongyuns.happyrupees;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.cretin.www.cretinautoupdatelibrary.model.TypeConfig;
import com.cretin.www.cretinautoupdatelibrary.model.UpdateConfig;
import com.cretin.www.cretinautoupdatelibrary.utils.AppUpdateUtils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.lirongyuns.happyrupees.activity.UploadActivity;
import com.lirongyuns.happyrupees.bean.MemberInfo;
import com.lirongyuns.happyrupees.bean.UpdateModel;
import com.lirongyuns.happyrupees.conf.NetWorkConf;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.CommonRequester;
import com.lirongyuns.happyrupees.utils.OkHttp3Connection;
import com.lirongyuns.happyrupees.utils.SharePreferenceUtil;
import com.liveness.dflivenesslibrary.DFProductResult;
import com.liveness.dflivenesslibrary.DFTransferResultInterface;

import java.util.Map;

import ai.advance.liveness.lib.GuardianLivenessDetectionSDK;
import ai.advance.liveness.lib.Market;
import ai.advance.sdk.quality.lib.GuardianImageQualitySDK;

public class MyApp extends Application implements DFTransferResultInterface {
    private DFProductResult mResult;

    public static final String AUTHORITY = "com.lirongyuns.happyrupees.fileprovider";

    CommonRequester mRequester;
    SharePreferenceUtil mSPUtil;
    MemberInfo mMemberInfo;
    private static final String AF_DEV_KEY = "7zbypxFikkpvPJix8Tm3Fg";//apps flyer

    public void setRiskId(String RiskId) {
        mSPUtil.put("RiskId", RiskId);
    }

    public String getRiskId() {
        return mSPUtil.getString("RiskId");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRequester = RetrofitCreator.create(CommonRequester.class);
        mSPUtil = new SharePreferenceUtil(this);
        GuardianLivenessDetectionSDK.init(this, "af6dd27fce2f0c70","fbef998162d9b1a0", Market.India);
//        GuardianLivenessDetectionSDK.init(this, "7318dda3a2c9a2a9","0c262d52fd53aedf", Market.India);
        GuardianLivenessDetectionSDK.letSDKHandleCameraPermission();

        //OCR
        GuardianImageQualitySDK.init(this, "af6dd27fce2f0c70", "fbef998162d9b1a0", Market.India);


        //更新库配置
        UpdateConfig updateConfig = new UpdateConfig()
                .setDebug(true)//是否是Debug模式
                .setBaseUrl(NetWorkConf.getInstance().getHttpHost() + "app/index/get_version")//当dataSourceType为DATA_SOURCE_TYPE_URL时，配置此接口用于获取更新信息
                .setMethodType(TypeConfig.METHOD_POST)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的方法
                .setDataSourceType(TypeConfig.DATA_SOURCE_TYPE_URL)//设置获取更新信息的方式
                .setShowNotification(true)//配置更新的过程中是否在通知栏显示进度
                .setNotificationIconRes(R.mipmap.logo)//配置通知栏显示的图标
                .setUiThemeType(TypeConfig.UI_THEME_AUTO)//配置UI的样式，一种有12种样式可供选择
                .setRequestHeaders(null)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的请求头
                .setRequestParams(null)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的请求参数
                .setAutoDownloadBackground(false)//是否需要后台静默下载，如果设置为true，则调用checkUpdate方法之后会直接下载安装，不会弹出更新页面。当你选择UI样式为TypeConfig.UI_THEME_CUSTOM，静默安装失效，您需要在自定义的Activity中自主实现静默下载，使用这种方式的时候建议setShowNotification(false)，这样基本上用户就会对下载无感知了
                .setCustomActivityClass(UploadActivity.class)//如果你选择的UI样式为TypeConfig.UI_THEME_CUSTOM，那么你需要自定义一个Activity继承自RootActivity，并参照demo实现功能，在此处填写自定义Activity的class
                .setNeedFileMD5Check(false)//是否需要进行文件的MD5检验，如果开启需要提供文件本身正确的MD5校验码，DEMO中提供了获取文件MD5检验码的工具页面，也提供了加密工具类Md5Utils
                .setCustomDownloadConnectionCreator(new OkHttp3Connection.Creator())//如果你想使用okhttp作为下载的载体，可以使用如下代码创建一个OkHttpClient，并使用demo中提供的OkHttp3Connection构建一个ConnectionCreator传入，在这里可以配置信任所有的证书，可解决根证书不被信任导致无法下载apk的问题
                .setModelClass(new UpdateModel());
        //初始化
        AppUpdateUtils.init(this, updateConfig);


        //初始化Appsflyer
        initAppsflyer();

        // Facebook统计开启
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
    }

    /**
     * 初始化Appsflyer
     */
    private void initAppsflyer() {

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
    }


    @Override
    public DFProductResult getResult() {
        return mResult;
    }

    @Override
    public void setResult(DFProductResult result) {
        this.mResult = result;
    }

    public interface UserInfoCallback {
        void onGetUserInfo();
    }

    public void updateUserInfo(UserInfoCallback callback) {
        String phone = getPhoneNumber();
        if (phone==null) return;
        JsonCreator creator = new JsonCreator();
        creator.put("phone", phone);
        mRequester.getUserInfo(RetrofitCreator.getFormBody(creator)).enqueue(new RetrofitCreator.ContentCallback<MemberInfo>() {
            @Override
            public void onSuccess(MemberInfo obj, String msg) {
                mMemberInfo = obj;
                if (callback!=null) callback.onGetUserInfo();
            }
        });
    }

    public MemberInfo.Info getMemberInfo() {
        return mMemberInfo.getInfo();
    }

    public String getPhoneNumber() {
        return mSPUtil.getString("phoneNumber");
    }

    public void setPhoneNumber(String phoneNumber) {
        mSPUtil.put("phoneNumber", phoneNumber);
    }

    public String getMemberId() {
        return mSPUtil.getString("memberId");
    }

    public void setMemberId(String memberId) {
        mSPUtil.put("memberId", memberId);
    }

    public boolean isLogin() {
        /**
         * 测试行
         */
        /*setMemberId("292");
        setPhoneNumber("13662567125");*/
        return mSPUtil.have("memberId");
    }

    public void clearData() {
        mSPUtil.clear();
    }


    public String getPhoneNum() {
        return mSPUtil.getString(SharePreferenceUtil.LOGIN_PHONE_NUM_KEY);
    }

    public void setPhoneNum(String phoneNumber) {
        mSPUtil.put(SharePreferenceUtil.LOGIN_PHONE_NUM_KEY, phoneNumber);
    }

    public Boolean getLoginStatus() {
        return mSPUtil.getBoolean(SharePreferenceUtil.LOGIN_STATUS);
    }

    public void setLoginStatus(Boolean phoneNumber) {
        mSPUtil.put(SharePreferenceUtil.LOGIN_STATUS, phoneNumber);
    }

}
