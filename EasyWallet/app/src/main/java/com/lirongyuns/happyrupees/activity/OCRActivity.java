package com.lirongyuns.happyrupees.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dfsdk.liveness.DFLivenessSDK;
import com.dfsdk.ocr.scan.DFOCRScan;
import com.dfsdk.ocr.scan.network.model.DFCardInfo;
import com.lirongyuns.happyrupees.MyApp;
import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;
import com.lirongyuns.happyrupees.bean.LivenessInfo;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RSA;
import com.lirongyuns.happyrupees.presenter.LivenessPresenter;
import com.lirongyuns.happyrupees.presenter.LivenessPresenterImpl;
import com.lirongyuns.happyrupees.presenter.PanCardPresenter;
import com.lirongyuns.happyrupees.presenter.PanCardPresenterImpl;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.utils.ToastUtil;
import com.lirongyuns.happyrupees.view.LivenessView;
import com.lirongyuns.happyrupees.view.PanCardView;
import com.lirongyuns.happyrupees.window.PickPictureWindow;
import com.liveness.dflivenesslibrary.DFProductResult;
import com.liveness.dflivenesslibrary.DFTransferResultInterface;
import com.liveness.dflivenesslibrary.feature.FeatureProcessBase;
import com.liveness.dflivenesslibrary.feature.SilentLivenessProcess;
import com.liveness.dflivenesslibrary.liveness.DFSilentLivenessActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ai.advance.liveness.lib.LivenessResult;
import ai.advance.sdk.iqa.IQAActivity;
import ai.advance.sdk.quality.lib.ImageQualityResult;
import ai.advance.sdk.quality.lib.enums.CardType;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SetActivity(R.layout.activity_o_c_r)
public class OCRActivity extends BaseActivity implements PanCardView, LivenessView {

    private PanCardPresenter mPanCardPresenter;

    @SetView(R.id.ocr_a_front)
    private ImageView ocr_a_front;

    @SetView(R.id.ocr_a_back)
    private ImageView ocr_a_back;

    @SetView(R.id.ocr_p_card)
    private ImageView ocr_p_card;

    @SetView(R.id.ocr_face)
    private ImageView ocr_face;



    @SetView(R.id.ocr_submit)
    private ImageView ocr_submit;

    private PickPictureWindow mPictureWindow;

    private LivenessPresenter livenessPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPictureWindow = new PickPictureWindow(this);
        mPictureWindow.setAuthority(MyApp.AUTHORITY);
        mPanCardPresenter = new PanCardPresenterImpl(this);
        liveness = new LivenessInfo();
        livenessPresenter = new LivenessPresenterImpl(this);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    Intent intent = new Intent(OCRActivity.this, WaitActivity.class);
                    intent.putExtra("flag","Face_Page_To");
                    startActivity(intent);
                    LoadingDialog.close();
                    finish();
                    break;
            }
        }
    };

    @Override
    public void back(View view) {
        super.back(view);
    }

    private int mTakePhotoType;
    private File mFileACardBack;
    private File mFileACardFront;
    private File mFilePanFront;

    @Click({R.id.ocr_submit,R.id.ocr_a_front,R.id.ocr_a_back,R.id.ocr_p_card,R.id.ocr_face})
    private void click(View view){
        switch (view.getId()){
            case R.id.ocr_a_front:
                mTakePhotoType = 3;
                if("0".equals(ConstantUtils.OCR_TYPE)){
                    //调用Advance的sdk
                    startIQA(CardType.AADHAAR_FRONT.name());
                }else{
                    //调用Accuauth的sdk
                    startCardDetect();
                }
                break;
            case R.id.ocr_a_back:
                mTakePhotoType = 4;
                if("0".equals(ConstantUtils.OCR_TYPE)){
                    //调用Advance的sdk
                    startIQA(CardType.AADHAAR_FRONT.name());
                }else{
                    //调用Accuauth的sdk
                    startCardDetect();
                }
                break;
            case R.id.ocr_p_card:
                mTakePhotoType = 1;
                if("0".equals(ConstantUtils.OCR_TYPE)){
                    //调用Advance的sdk
                    startIQA(CardType.AADHAAR_FRONT.name());
                }else{
                    //调用Accuauth的sdk
                    startCardDetect();
                }
                break;
            case R.id.ocr_face:
                if ("0".equals(ConstantUtils.OCR_TYPE)) {
                    //调用Advance的sdk
                    Intent i = new Intent(this, OCRActivity.class);
                    startActivityForResult(i, CODE_LIVENESS);
                } else {
                    startAccuauthFaceLiveness();
                }
                break;
            case R.id.ocr_submit:
                if(TextUtils.isEmpty(pcfPath)){
                    ToastUtil.show(OCRActivity.this, "Please take A picture of the front of Card PAN");
                    return;
                }
                if(TextUtils.isEmpty(cfPath)){
                    ToastUtil.show(OCRActivity.this, "Please take A picture of the front of Card AADHAAR");
                    return;
                }

                if(TextUtils.isEmpty(cbpath)){
                    ToastUtil.show(getBaseContext(), "Please take A picture of the back of Card AADHAAR");
                    return;
                }
                ocr_submit.setEnabled(false);
                LoadingDialog.open(this);
                mPanCardPresenter.commitOcrImagePathToServer(getApp().getMemberId(), pcfPath, cfPath, cbpath);
                break;
        }
    }

    private String cfPath;//服务器返回的A卡正面图片地址
    private String cbpath;//服务器返回的A卡背面图片地址
    private String pcfPath;//服务器返回的P卡正面图片地址

    private static final int REQUEST_CODE_CARD = 100;

    private void startCardDetect() {
        DFOCRScan.create(this)
                .startCardDetect()
                .forResult(REQUEST_CODE_CARD);
    }


    private void commitFaceImageToAdvance() {
        try {
            livenessPresenter.uploadVerify(getApp().getMemberId(),liveness );
//            Intent intent = new Intent(this, LivenessActivity.class);
//            startActivityForResult(intent, CODE_LIVENESS);

            appsflyerEvent(getApp().getPhoneNum(), "CONFIRM_FACE");
            setFirebaseEvent(getApp().getPhoneNum(), "CONFIRM_FACE");

            Bundle bundle = new Bundle();
            setFaceBookEvent(bundle, "CONFIRM_FACE");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FeatureProcessBase mCurrentFeature = null;
    private static final int FACE_LIVNESS_DETECT_REQUEST_CODE = 1002;

    /*Accuauth的sdk*/
    private void startAccuauthFaceLiveness() {
        mCurrentFeature = new SilentLivenessProcess();
        Intent intent = new Intent();
        intent.setClass(this, DFSilentLivenessActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        //设置返回图片结果
        intent.putExtra(DFSilentLivenessActivity.KEY_DETECT_IMAGE_RESULT, true);
        intent.putExtra(DFSilentLivenessActivity.KEY_ANTI_HACK, true);
        intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_HAS_FACE, "请保持静止");
        intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_NO_FACE, "请将人脸置入圈内");
        intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_FACE_NOT_VALID, "请远离一点点");
        startActivityForResult(intent, FACE_LIVNESS_DETECT_REQUEST_CODE);
    }

    private static int CODE_LIVENESS = 10;

    private String mIdCardFrontPath, mIdCardBackPath, mPanCardFrontPath;
    private String mIdCardFace;
    private String mPanCardFace;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri picUri = mPictureWindow.getPicUri();
        mPictureWindow.getFilePath();
        String filePath = mPictureWindow.getFilePath();
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        switch (requestCode) {
            case REQUEST_CODE_CARD:
                dealCardResult(resultCode, data, externalFilesDir.getPath());
            case PickPictureWindow.TAKE_PHOTO:
                if (3 == mTakePhotoType) {
                    mIdCardFrontPath = filePath;

                    displayImage(picUri, ocr_a_front);
                    ocr_a_front.invalidate();

                    setFirebaseEvent("", "COMMIT_OCR_A_FRONT_PHOTO");
                    appsflyerEvent(getApp().getPhoneNum(), "COMMIT_OCR_A_FRONT_PHOTO");
                    Bundle bundle = new Bundle();
                    setFaceBookEvent(bundle, "COMMIT_OCR_A_FRONT_PHOTO");

                } else if(4 == mTakePhotoType){
                    mIdCardBackPath = filePath;
                    displayImage(picUri, ocr_a_back);
                    setFirebaseEvent("", "COMMIT_OCR_A_BACK_PHOTO");
                    appsflyerEvent(getApp().getPhoneNum(), "COMMIT_OCR_A_BACK_PHOTO");
                    Bundle bundle = new Bundle();
                    setFaceBookEvent(bundle, "COMMIT_OCR_A_BACK_PHOTO");
                }else{
                    mPanCardFrontPath = filePath;
                    displayImage(picUri, ocr_p_card);
                    ocr_p_card.invalidate();

                    setFirebaseEvent("", "COMMIT_OCR_P_FRONT_PHOTO");
                    appsflyerEvent(getApp().getPhoneNum(), "COMMIT_OCR_P_FRONT_PHOTO");

                    Bundle bundle = new Bundle();
                    setFaceBookEvent(bundle, "COMMIT_OCR_P_FRONT_PHOTO");

                }
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filePath))));
                break;

            case PickPictureWindow.CROP:
                if(3 == mTakePhotoType){
                    mIdCardFace = mPictureWindow.getCropImagePath();
                }else{
                    mPanCardFace = mPictureWindow.getCropImagePath();
                }

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(mPictureWindow.getCropImagePath()))));
                break;
            case REQUEST_CODE_IQA:
                if (ImageQualityResult.isSuccess()) {
                    // 检测成功
                    String imageQualityId = ImageQualityResult.getImageQualityId();// 图像id
                    CardType cardType = ImageQualityResult.getCardType();// 卡片类型
                    Bitmap previewBitmap = ImageQualityResult.getPreviewBitmap();// 预览框里的图像
                    Bitmap originBitmap = ImageQualityResult.getOriginBitmap();// 相机原始图像
                    switch (cardType){
                        case PAN:
                            ocr_p_card.setImageBitmap(previewBitmap);
                            String fileName = "PanCardFront"+System.currentTimeMillis()+".jpg";
                            mFilePanFront = compressImage(originBitmap, externalFilesDir.getPath(), fileName);
                            break;
                        case AADHAAR_FRONT:
                            ocr_a_front.setImageBitmap(previewBitmap);
                            String aCardFrontName = "ACardFront"+System.currentTimeMillis()+".jpg";
                            mFileACardFront = compressImage(originBitmap, externalFilesDir.getPath(), aCardFrontName);
                            break;
                        case AADHAAR_BACK:
                            ocr_a_back.setImageBitmap(previewBitmap);
                            String aCardBackName = "ACardFront"+System.currentTimeMillis()+".jpg";
                            mFileACardBack = compressImage(originBitmap, externalFilesDir.getPath(), aCardBackName);
                            break;
                    }

                } else {// 检测失败
                    String errorCode = ImageQualityResult.getErrorCode();// 失败错误码
                    String errorMsg = ImageQualityResult.getErrorMsg();// 失败原因，可能为空(例如用户点击返回键放弃，则只有错误码，没有msg)
                    Log.e("kmsg", "errorCode：" + errorCode + "，errorMsg:" + errorMsg);
                }
                break;
        }

        if (requestCode==CODE_LIVENESS) {
            if (LivenessResult.isSuccess()) {
                // 活体检测成功
                facechekBitmap = LivenessResult.getLivenessBitmap();
                ocr_face.setImageBitmap(facechekBitmap);
                ocr_submit.setEnabled(true);
                liveness.setLiveness(facechekBitmap);
                encodeToString = "";
            } else {
                // 活体检测失败
                String errorMsg = LivenessResult.getErrorMsg();// 失败原因
                ToastUtil.show(this, errorMsg);
                ocr_submit.setEnabled(false);
            }
            return;
        }

        if (mCurrentFeature != null) {
            mCurrentFeature.onActivityResult(this, requestCode, resultCode, data);
        }

        /* 处理人脸识别 */
        if (resultCode == RESULT_OK) {
            DFProductResult mResult = ((DFTransferResultInterface)getApplication()).getResult();

            //获取关键帧图像
            DFLivenessSDK.DFLivenessImageResult[] imageResultArr = mResult.getLivenessImageResults();

            if (imageResultArr != null) {
                int size = imageResultArr.length;
                if (size > 0) {
                    DFLivenessSDK.DFLivenessImageResult imageResult = imageResultArr[0];
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageResult.image, 0, imageResult.image.length);
                    ocr_face.setImageBitmap(imageBitmap);
                    ocr_submit.setEnabled(true);
                    facechekBitmap = imageBitmap;
                    liveness.setLiveness(facechekBitmap);
                }
            }

            livenessEncryptResult = mResult.getLivenessEncryptResult();
            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

            encodeToString = encoder.encodeToString(livenessEncryptResult);
        }
    }

    private void dealCardResult(int resultCode, Intent data, String path) {
        if (resultCode == RESULT_OK) {
            DFCardInfo cardInfo = DFOCRScan.obtainCardResult(data);
            byte[] detectImage = cardInfo.getDetectImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(detectImage, 0, detectImage.length);
            String cardType = cardInfo.getCardType();
            //Log."kmsg", cardType);
            String name = "";
            String fileType = "";
            if("pan_card".equalsIgnoreCase(cardType)){
                name = "PanCardFront";
                fileType = "pan_positive";
                ocr_p_card.setImageBitmap(bitmap);
            }else if("aadhaar_card_back".equalsIgnoreCase(cardType)){
                name = "ACardBack";
                fileType = "aadhaar_back";
                ocr_a_back.setImageBitmap(bitmap);
            }else {
                name = "ACardFront";
                fileType = "aadhaar_positive";
                ocr_a_front.setImageBitmap(bitmap);
            }
            String fileName = name + System.currentTimeMillis()+".jpg";

            File file = compressImage(bitmap, path, fileName);
            commitImageFileToServer(file, fileType);
        } else {
            if (data != null) {
                int errorCode = DFOCRScan.obtainErrorCode(data);
                DFCardInfo dfCardInfo = DFOCRScan.obtainCardResult(data);
            }
        }
    }

    private void commitImageFileToServer( File file, String bType) {
        if(null != file){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("img1", file.getName(), requestFile);

            JsonCreator jsonCreator = new JsonCreator();
            jsonCreator.put("memberId", getApp().getMemberId());
            jsonCreator.put("bType", bType);
            String data = "";
            try {
                byte[] bytes = RSA.encryptByPublicKey(jsonCreator.toString().getBytes(), RSA.PUBLICKEY);
                data = Base64.encodeToString(bytes, Base64.DEFAULT);
                MultipartBody.Part part = MultipartBody.Part.createFormData("data", data);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_ocr_upload_dialog, null);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                Button ok = view.findViewById(R.id.btn_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                mPanCardPresenter.commitPanImageToServer(body, part, bType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private String encodeToString;

    private byte[] livenessEncryptResult;
    private LivenessInfo liveness;

    Bitmap facechekBitmap;


    private final int REQUEST_CODE_IQA = 1000;

    /**
     * 启动检测
     */
    private void startIQA(String type) {
        Intent intent = new Intent(OCRActivity.this, IQAActivity.class);
        String cardType = type;

        intent.putExtra("cardType", cardType);// 默认值为 AUTO
        intent.putExtra("timeoutSeconds", 20);//默认值为20
        intent.putExtra("maxRetryTimes", 2);//默认重试次数为2次
        intent.putExtra("soundPlayEnable", true);//默认播放声音
        startActivityForResult(intent, REQUEST_CODE_IQA);

    }

    private void displayImage(Uri picUri, ImageView imageView) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(picUri, "r");
            if (parcelFileDescriptor != null && parcelFileDescriptor.getFileDescriptor() != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                //转换uri为bitmap类型
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                imageView.setImageBitmap(bitmap);
                // 你可以做的~~
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private  File compressImage(Bitmap bitmap, String path, String fileName) {
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

    private String mPanCardFacePath;
    private String mIdCardFacePath;

    @Override
    public void onUpLoadSuccess(String msg) {
        ToastUtil.show(OCRActivity.this,msg);
    }

    @Override
    public void success(String msg) {
        ToastUtil.show(OCRActivity.this, msg);
        if(mPanCardFace !=null && mIdCardFace !=null){

            commitFaceImageToAdvance();
            ocr_submit.setEnabled(false);

            //                Intent intent = new Intent(this, LivenessActivity.class);
            //                startActivityForResult(intent, CODE_LIVENESS);
            appsflyerEvent(getApp().getPhoneNum(), "START_FACE_SDK");
            setFirebaseEvent(getApp().getPhoneNum(), "START_FACE_SDK");

            setFaceBookEvent(new Bundle(), "START_FACE_SDK");
        }
        else{
            LoadingDialog.close();
        }
        //提交人脸
//        Intent intent = new Intent(OCRActivity.this, FaceRecognitionActivity.class);
//        intent.putExtra("PanCardFacePath", mPanCardFace);
//        intent.putExtra("IdCardFacePath", mIdCardFace);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void setOCRImagePath(String fileName, String ocrType) {

    }

//    @Override
//    public void setAuthorityProtocolUrl(String msg) {
//        url = msg;
//        Intent intent = new Intent(this, ProtocolDescriptionUcashActivity.class);
//        intent.putExtra("jumpUrl", url);
//        startActivity(intent);
//    }

    @Override
    public void onGetUploadResult(boolean isSuccess) {
        LoadingDialog.close();
        livenessPresenter.getUserInfoFromServer(getApp().getMemberId());
        LoadingDialog.open(this);
        Message message = mHandler.obtainMessage();
        message.what = 100;
        mHandler.sendMessage(message);
    }


    @Override
    public void showToast(String msg) {
        ToastUtil.show(this,msg);
    }
}