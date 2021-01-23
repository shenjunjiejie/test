package com.ucash_test.lirongyunindialoan.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dfsdk.ocr.scan.DFOCRScan;
import com.dfsdk.ocr.scan.network.model.DFCardInfo;
import com.dfsdk.ocr.scan.util.DFAppUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ucash_test.lirongyunindialoan.BuildConfig;
import com.ucash_test.lirongyunindialoan.R;
import com.ucash_test.lirongyunindialoan.annonation.Click;
import com.ucash_test.lirongyunindialoan.annonation.SetActivity;
import com.ucash_test.lirongyunindialoan.annonation.SetView;
import com.ucash_test.lirongyunindialoan.config.AppStatusConf;
import com.ucash_test.lirongyunindialoan.internet.JsonFormat;
import com.ucash_test.lirongyunindialoan.internet.JsonParser;
import com.ucash_test.lirongyunindialoan.internet.OKHttpUtils;
import com.ucash_test.lirongyunindialoan.internet.RSA;
import com.ucash_test.lirongyunindialoan.myosotisutils.ConfigSPUtils;
import com.ucash_test.lirongyunindialoan.myosotisutils.FileUtils;
import com.ucash_test.lirongyunindialoan.myosotisutils.ShowTextUtils;
import com.ucash_test.lirongyunindialoan.ui.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SetActivity(R.layout.launcher)
public class LaunchActivity extends BasicActivity {
    @SetView(R.id.laucher_tv_timer)
    private TextView mTitleTv;
    Callback callback;
    @Inject
    SharedPreferences sharedPreferences;

    private ScheduledExecutorService mThreadPool;
    private AtomicInteger time;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        switch (requestCode){
            case REQUEST_CODE_CARD:
                dealCardResult(resultCode, data, externalFilesDir.getPath());
                break;
            case RESULT_TEST:
                if(resultCode == RESULT_OK){
                    Log.e("test","test");
                }
        }
    }

    private static final int RESULT_TEST = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //代扣测试
//        JsonParser parser = new JsonParser();
//        parser.put("mobile", "18573343663");
//        parser.put("password", "234567");
//        callback = new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("error:",e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("response:",response.body().string());
//            }
//        };
//        OKHttpUtils.httpPost("http://192.168.3.19:8081/app/login", parser, callback);
//            loginCode();
//            login();
//            saveLoginData();
//            ApplyPermissions();
//            getInformation();
//            setUserInfomation();
        mThreadPool = Executors.newScheduledThreadPool(1);
        time = new AtomicInteger(5);
        mThreadPool.scheduleAtFixedRate(()->{
            int i = time.getAndDecrement();
            Message msg = mHandler.obtainMessage();
            msg.what = 0;
            mHandler.sendMessage(msg);
            if (time.get() == 0){
                msg.what = 1;
                mHandler.sendMessage(msg);
            }

        },1,1,TimeUnit.SECONDS);

        

//        int threads = 12;
//        // 1.创建固定线程池
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(12);
//        // 2. 创建好Runnable类线程对象 & 需执行的任务
//        Runnable task =new Runnable(){
//            public void run(){
//
//            }
//        };
//        // 3. 向线程池提交任务：execute（）
//        for(int t = 0;t < threads;t ++){
//            final String no = String.format("No #%d", t);
//            fixedThreadPool.execute(task);
//        }
//        // 4. 关闭线程池
//        fixedThreadPool.shutdown();
    }

    private void saveLoginData(){
        getApp().setPhoneNum("913692536925");
        getApp().setLoginStatus(true);
        getApp().setMemberId("83902");
    }

    /* 获取账户参数 */
    private void getInformation(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", getApp().getMemberId());//*	用户ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/login/getInformation", parser, callback);
    }


    private void showBuildConfig(){
        Log.e("IS_TEST", BuildConfig.IS_TEST+"");
        Log.e("HTTP_HOST_TEST2", BuildConfig.HTTP_HOST_TEST2+"");
//        Log.e("HTTP_HOST_TEST",BuildConfig.HTTP_HOST_TEST);
        Log.e("VERSION_NAME",BuildConfig.VERSION_NAME);
        Log.e("VERSION_CODE",BuildConfig.VERSION_CODE+"");
        Log.e("BUILD_TYPE",BuildConfig.BUILD_TYPE);
        Log.e("APPLICATION_ID",BuildConfig.APPLICATION_ID);
        Log.e("DEBUG",BuildConfig.DEBUG+"");

    }

    /* 获取登录验证码 */
    private void loginCode(){
        JsonParser parser = new JsonParser();
        parser.put("phone", "91"+"3692536925");
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/login/code", parser, callback);

    }

    /* 登录 */
    private void login(){
        JsonParser parser = new JsonParser();
        parser.put("phone", "91"+"3692536925");
        parser.put("IDcode", "614040");
        parser.put("channel", mChannel.get("test"));
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/login", parser, callback);
    }
    /*
    * new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                LoginJson loginJson = gson.fromJson(response.body().string(), LoginJson.class);
                int code = loginJson.getCode();
                LoginJson.DataBean dataBean = loginJson.getData();
                LoginJson.DataBean.LoanMemberBean loanMember = dataBean.getLoanMember();
                if(null != loanMember){
                    getApp().setPhoneNum(loanMember.getMobile());
                    getApp().setLoginStatus(true);
                    getApp().setMemberId(loanMember.getId()+"");

                }
            }
        }
    * */
    private HashMap<String,Integer> mChannel = new HashMap<>();

    /* 一次登录过后默认登录主界面 */
    private void withOutLoginToMain(){
        if(!getApp().getLoginStatus()){
            Log.e("withOutLoginToMain","without login");
        }
        else{
            Log.e("withOutLoginToMain","have login");
        }
    }

    /* 填写个人信息 */
    private void setUserInfomation(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", getApp().getMemberId());//*	用户ID
        if("unkown".equals(getSerialNumber())){
            Log.e("serialNumber","The device's serialNumber is unknown.");
        }
        else if("".equals(getSerialNumber())){
            Log.e("serialNumber","The device's serialNumber is empty.");
        }
        else{
            Log.e("serialNumber","The device's serialNumber is "+getSerialNumber());
            parser.put("serialNumber",getSerialNumber());//	手机序列号(可为空)
        }
        parser.put("imeiIsSame", "1");//*	申请时 IMEI 与注册是否一致，1：一致 0：不一致
        parser.put("salaryRange", "15000-25000");//*薪资范围 * 1：0-15000 2：15000-25000 3：25000-23000 4：35000-45000 5：45000-55000 6：55000 以上
        parser.put("brandModel","brandModel");//*	手机品牌
        parser.put("marryState","1");//*婚恋状态 *1：未婚 2：已婚 3：离异 4：丧偶
        parser.put("jobPosition","Manager");//*工作职位 *1：Ordinary staff 2：Executive 3：Supervisor 4：Manager 5：Director 6：其他
        parser.put("applyTime","2020/3/13 13:10:00");//*申请时间 格式 2020/3/13 13:10:00
        parser.put("education","3");//*学历 * 0:未上学,1 高中一下,2 高中,3 大学,4 大学及其以上
        parser.put("phoneNumber1","1472514725");//*91+本机注册电话1
//        parser.put("phoneNumber2","phoneNumber2");//*91+本机注册电话2
        parser.put("contact1","1596315963");//*91+紧急联系人 1 手机号
        parser.put("contact2","1239812398");//91+紧急联系人 2 手机号
        parser.put("contactName1","contactName1");//*	紧急联系人 1 名字
        parser.put("contactName2","contactName2");//*	紧急联系人 2 名字
        parser.put("religiouBelief","6");//*	宗教 0：MOSLEM、1：CHRISTIAN、2：CATHOLIC、3：HINDU、4：BUDDHA、5：CONFUSIUS、6：NONE、7：OTHERS
        parser.put("language","English");//*	语言
        parser.put("email","5588866661@163.com");//*邮箱
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/pick_up_information/getpersona", parser, callback);
    }

    /* 预览账单 */
    private void getpreviewBill(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", "memberId");//*	用户ID
        parser.put("productId","productId");//*		产品ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/mwithdraw/getpreviewBill", parser, callback);

    }

    /* 提交审核 */
    private void commitAudit(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", "memberId");//*	用户ID
        parser.put("productId","productId");//*		产品ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/apply_credit/extension", parser, callback);
    }

    /* 校验银行卡 */
    private void checkbank(){
        JsonParser parser = new JsonParser();
        parser.put("beneAccno", "beneAccno");//*	收款方银行账号
        parser.put("bankName","bankName");//		收款方银行名称
        parser.put("beneIfsc", "beneIfsc");//*		收款方 IFSC,11位长度
        parser.put("beneAccno", "beneAccno");//*	收款方银行账号
        parser.put("memberId", "91"+"3692536925");//*	用户ID
        parser.put("cardName", "cardName");//		卡持有者名称
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/login", parser, callback);
    }

    /* 获取ocr 图片 (新版) */
    private void getNewOcr(){
        JsonParser parser = new JsonParser();
        /*
        * { "memberId": "用户ID 加密",
        * "pan_positive": "PAN正面 fileName",
        *  "aadhaar_positive": "Aadhaar卡正面 fileName",
        * "aadhaar_back": "Aadhaar卡反面 fileName" }
        * */
        parser.put("data", "memberId");
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/big_data/get_new_ocr", parser, callback);
    }

    /* 审核协议预览 */
    private void viewWarrant(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", "91"+"3692536925");//*	用户ID
        parser.put("productId","productId");//*		产品ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/apply_credit/view_warrant", parser, callback);
    }


    /* 统计用户访问情况 */
    private void userVisitTongJi(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", "memberId");//*	用户ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/index", parser, callback);
    }

    /* 获取银行卡信息 */
    private void bankCardDisPlay(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", "91"+"3692536925");//*	用户ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/index/bank_card_display", parser, callback);
    }

    /* 查询用户所有账单 */
    private void userBill(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", "memberId");//*	用户ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/mwithdraw/user_bill", parser, callback);
    }

    /* 获取APP更新新信息 */
    private void getVersion(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", "memberId");//*	用户ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/index/get_version", parser, callback);
    }

    /* 查询还款账单 */
    private void getBill(){
        JsonParser parser = new JsonParser();
        parser.put("bill_no", "bill_no");//*	用户ID
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/repayment/get_bill", parser, callback);
    }

    /* 上传OCR图片接口 */
    private void saveImg(){
        JsonParser parser = new JsonParser();
        parser.put("img1", "img1");//*	用户ID
        /*
        * 	{memberId：""，
        * “bType”：“pan_positive” PAN正面
        * “ aadhaar_positive”Aadhaar卡正面
        *  “aadhaar_back”Aadhaar卡反面}
        * */
        parser.put("data","data");
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/big_data/save_img", parser, callback);
    }

    /* IFSC通过接口获取IFCS */
    private void getIfcs(){
        JsonParser parser = new JsonParser();
        parser.put("state", "state");//*	国家
        parser.put("city","city");//*       城市
        parser.put("bank_name","bank_name");//* 银行名称
        parser.put("branch","branch");//*   分行
        parser.put("serialNumber","serialNumber");//*	手机序列号(可为空)
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/mwithdraw/getIfcs\n", parser, callback);
    }

    /* 格式 */
    private void defaultRequest(){
        JsonParser parser = new JsonParser();
        parser.put("memberId", "memberId");//*	用户ID
        parser.put("serialNumber","serialNumber");//*	手机序列号(可为空)
        OKHttpUtils.httpPost(BuildConfig.HTTP_HOST_TEST2 + "app/login", parser, callback);
    }

    /* 渠道 */
    private HashMap<String,Integer> getChannel(){
        mChannel.put("test",1);
        return mChannel;
    }


    @Click(R.id.laucher_tv_timer)
    private void click(View view){
        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        mHandler.sendMessage(msg);
    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    mTitleTv.setText("跳过 0" + time.get());
                    break;
                case 1:
                    startActivity(new Intent(getCtx(), LoginActivity.class));
                    mThreadPool.shutdownNow();
                    finish();
                    break;
                default:
                    ShowTextUtils.show(getCtx(),getClass()+" Unexpected value: " + msg.what);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    private void getSplitSubFile(File sourceFile, String type){
        if(BuildConfig.DEBUG){
            Log.e("kmsg", "sourceFile===="+sourceFile.length());
        }

        int blockNum = FileUtils.getSplitFile(sourceFile, 1024 * 100, type, getCtx());
    }


    @SuppressLint({"NewApi", "MissingPermission"})
    private static String getSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0+
                serial = Build.getSerial();
            }
            else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", "读取设备序列号异常：" + e.toString());
        }
        return serial;
    }

    /**
     * 申请多个权限
     */
    @SuppressLint("CheckResult")
    private void ApplyPermissions(){
        RxPermissions rxPermissions = new RxPermissions(LaunchActivity.this);
        rxPermissions.requestEach(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe((Permission permission)->{
                    if (permission.granted ) {} else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            //申请被拒绝的权限
                            /*if(Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission.name) || Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission.name)){
                                //READ_EXTERNAL_STORAGE
                                startHintPage(getString(R.string.str_cosmic_read_storage));
                            }else if(Manifest.permission.CAMERA.equals(permission.name)){
                                //Camera
                                startHintPage(getString(R.string.str_cosmic_camera));
                            }else if(Manifest.permission.READ_CONTACTS.equals(permission.name)){
                                //READ_CONTACTS
                                startHintPage(getString(R.string.str_cosmic_read_contacts));
                            }else if(Manifest.permission.READ_SMS.equals(permission.name)){
                                //READ_SMS
                                startHintPage(getString(R.string.str_cosmic_read_sms));
                            }else if(Manifest.permission.READ_PHONE_STATE.equals(permission.name)){
                                //READ_PHONE_STATE
                                startHintPage(getString(R.string.str_cosmic_read_phone));
                            }else{
                                startHintPage(getString(R.string.str_cosmic_internet));
                            }*/

                        }
                    } else {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                            //申请被拒绝的权限
//                            if(Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission.name) || Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission.name)){
//                                //READ_EXTERNAL_STORAGE
//                                startHintPage(getString(R.string.str_cosmic_read_storage));
//                            }else if(Manifest.permission.CAMERA.equals(permission.name)){
//                                //CAMERA
//                                startHintPage(getString(R.string.str_cosmic_camera));
//                            }else if(Manifest.permission.READ_CONTACTS.equals(permission.name)){
//                                //READ_CONTACTS
//                                startHintPage(getString(R.string.str_cosmic_read_contacts));
//                            }else if(Manifest.permission.READ_SMS.equals(permission.name)){
//                                //READ_SMS
//                                startHintPage(getString(R.string.str_cosmic_read_sms));
//                            }else if(Manifest.permission.READ_PHONE_STATE.equals(permission.name)){
//                                //READ_PHONE_STATE
//                                startHintPage(getString(R.string.str_cosmic_read_phone));
//                            }else{
//                                startHintPage(getString(R.string.str_cosmic_internet));
//                            }
                        }
                    }
                });
    }

    /* 启动OCR身份证识别 */
    private static final int REQUEST_CODE_CARD = 100;
    private void startCardDetect() {
        DFOCRScan.create(this)
                .startCardDetect()
                .forResult(REQUEST_CODE_CARD);
    }

    private ImageView mPanCardFront;//P卡正面
    private ImageView mPanCardBack;//P卡背面
    private ImageView mIdCardBack;//A卡背面
    private ImageView mIdCardFront;//A卡正面
    private void dealCardResult(int resultCode, Intent data, String path) {
        if (resultCode == RESULT_OK) {
            DFCardInfo cardInfo = DFOCRScan.obtainCardResult(data);
            byte[] detectImage = cardInfo.getDetectImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(detectImage, 0, detectImage.length);
            String cardType = cardInfo.getCardType();
            Log.e("kmsg", cardType);
            String name = "";
            String fileType = "";
            if("pan_card".equalsIgnoreCase(cardType)){
                name = "PanCardFront";
                fileType = "pan_positive";
                mPanCardFront.setImageBitmap(bitmap);
            }else if("aadhaar_card_back".equalsIgnoreCase(cardType)){
                name = "ACardBack";
                fileType = "aadhaar_back";
                mIdCardBack.setImageBitmap(bitmap);
            }else {
                name = "ACardFront";
                fileType = "aadhaar_positive";
                mIdCardFront.setImageBitmap(bitmap);
            }
            String fileName = name + System.currentTimeMillis()+".jpg";
            File file = compressImage(bitmap, path, fileName);
            commitImageFileToServer(file, fileType);
        } else {
            if (data != null) {
                int errorCode = DFOCRScan.obtainErrorCode(data);
                DFCardInfo dfCardInfo = DFOCRScan.obtainCardResult(data);
                DFAppUtils.logI("dealAadhaarCardResult", "errorCode", errorCode);
            }
        }
    }

    private File compressImage(Bitmap bitmap, String path, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于20kb,大于继续压缩 友盟缩略图要求不大于18kb
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }

        File file = new File(path, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        return file;
    }

    private void commitImageFileToServer(File file, String bType) {
        if(method == FileUploadMethod.ONLY_FILE){
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MediaType.parse("multipart/form-data"));
            if(file != null){
                builder.addFormDataPart("file",file.getName());
//                builder.addFormDataPart("file",file.getName(),(MediaType.parse("image"));
            }
            MultipartBody requestBody = builder.build();
        }
        else{
            if (null != file) {
                // 将文件保存到body
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("img1", file.getName(), requestFile);

                // 将数据保存到data，并对data进行加密
                JsonParser parser = new JsonParser();
                parser.put("memberId", getApp().getMemberId());
                parser.put("bType", bType);
                String data = "";
                //分段加密
                try {
                    byte[] bytes = RSA.encryptByPublicKey(parser.toString().getBytes(), RSA.getPUBLICKEY());
                    data = Base64.encodeToString(bytes, Base64.DEFAULT);
                    Log.e("kmsg", data);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("data", data);

//                    OKHttpUtils.httpPostwithFiles("",part,body,callback);
//                mPanCardPresenter.commitPanImageToServer(body, part, bType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private enum FileUploadMethod{
        PART,BODY,ONLY_FILE
    }

    private FileUploadMethod method = FileUploadMethod.BODY;
}
