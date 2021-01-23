package com.lirongyuns.happyrupees.utils;

public class ConstantUtils {
    
    public static int RESPONSE_OK = 200;//请求成功
    public static int RESPONSE_ERROR = 500;//请求失败
    public static String AADHAAR_ID;//印度aadhaar卡
    public static String PAN_ID;//印度pan卡
    
    public static String LOAN_STATUS;//借款状态0.注册、1.填写资料中、2.审核中、3.拒绝、4.机审通过、5.机审拒绝、6.人审通过、
    // * 7.人审拒绝 8借款中、9.逾期、10.还款完成 11放款失败、12待还款
   
    public static String IS_UPLOAD_APP; //上传APP 0 未上传 1已上传
    public static String IS_UPLOAD_ALBUM; //上传相册 0 未上传 1已上传
    public static String IS_UPLOAD_SMS;	//上传短信 0 未上传 1已上传
    public static String IS_UPLOAD_CONTACT; //上传通讯录 0 未上传 1已上传
    public static String IS_UPLOAD_INFORMATION; //上传基本信息 0 未上传 1已上传
    public static String IS_UPLOAD_PAN; //检查orc 0:未通过 1：已通过
    public static String IS_UPLOAD_FACE; //检查人脸 0：未通过 1：已通过
    public static String REPEATED_BORROWING;	//更新状态 0新用户 1复贷用户 2不满足复贷条件 3已重置OCR 4已重置风控审核 5.还款完成为复贷准会员
    public static String APP_SETTING;//0 A面 1 B面
    public static String PHONE_NUM;//手机号码
    public static String IS_CAN_REPEAT;//是否能复借 0否 1是
    public static String IS_ALL_UPLOAD;//是否全部上传成功 0否 1是

    public static String CARD_CHECK_MSG;//绑卡失败原因
    public static String OCR_TYPE;//ocr 和 活体检测 SDK切换 0:Advance  1:Accuauth
    public static String PAYMENT_CHANNEL;//支付sdk切换 3：cashfress
  
}
