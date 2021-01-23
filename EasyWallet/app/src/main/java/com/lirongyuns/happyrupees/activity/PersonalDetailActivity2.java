package com.lirongyuns.happyrupees.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;
import com.lirongyuns.happyrupees.bean.BigDataConfig;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.presenter.CrawlPhoneDataPresenterImpl;
import com.lirongyuns.happyrupees.presenter.LoanPresenter;
import com.lirongyuns.happyrupees.presenter.LoanPresenterImpl;
import com.lirongyuns.happyrupees.utils.ToastUtil;
import com.lirongyuns.happyrupees.view.Alert;
import com.lirongyuns.happyrupees.view.CrawlView;
import com.lirongyuns.happyrupees.view.LoanView;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@SetActivity(R.layout.activity_pernal_detail2)
public class PersonalDetailActivity2 extends BaseActivity implements LoanView, CrawlView {

    private CrawlPhoneDataPresenterImpl crawlPhonePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

        mPresenter = new LoanPresenterImpl(this);
        crawlPhonePresenter = new CrawlPhoneDataPresenterImpl(this);
    }

    @Click({R.id.personal_info_relationship1,
            R.id.personal_info_relationship2,
            R.id.personal_info_commit})
    private void click(View view){
        switch (view.getId()){
            case R.id.personal_info_relationship1:
                openAddressBook(1);
                break;
            case R.id.personal_info_relationship2:
                openAddressBook(2);
                break;
            case R.id.personal_info_commit:
                if(stringIsEmpty(name1)){
                    ToastUtil.show(this,"relationship1 is empty.");
                    break;
                }
                else if(stringIsEmpty(phone1)){
                    ToastUtil.show(this,"relationship1 is empty.");
                    break;
                }
                else if(stringIsEmpty(name2)){
                    ToastUtil.show(this,"relationship2 is empty.");
                    break;
                }
                else if(stringIsEmpty(phone2)){
                    ToastUtil.show(this,"relationship2 is empty.");
                    break;
                }
                commit();
                break;
        }
    }

    private LoanPresenter mPresenter;

    private void commit() {
        crawlPhonePresenter.getUserInfoFromServer(getApp().getMemberId());
        TelephonyManager tm = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String phoneTelNumber = tm.getLine1Number();

        if (TextUtils.isEmpty(phoneTelNumber)) {
            phoneTelNumber = "";
        }

        if(!TextUtils.isEmpty(phoneTelNumber) && !phoneTelNumber.startsWith("91")){
            if(!phoneTelNumber.startsWith("+91")){
                phoneTelNumber = "91"+phoneTelNumber;
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String memberId = getApp().getMemberId();

        if(TextUtils.isEmpty(memberId)){
            memberId = "0";
        }

        long id = 0;
        if(!TextUtils.isEmpty(memberId)){
            id = Long.parseLong(memberId);
        }

        String serialNumber = getUniqueID(this);
        int imeiIsSame = 1;
        String brandModel =  Build.BRAND + "_" + Build.MODEL;
        int personalAge = 0;
        int sex = 0;
        String applyTime = dateFormat.format(new Date());
        String phoneNumber1 = phone1;
        String phoneNumber2 = phone2;
        String contact1 = name1;
        String contact2 = name2;

        if (TextUtils.isEmpty(contact1) || TextUtils.isEmpty(contact2)) {
            ToastUtil.show(this, "Please select you contact");
            return;
        }

        if (!TextUtils.isEmpty(phoneNumber1) && !phoneNumber1.startsWith("91")) {
            if(!phoneNumber1.startsWith("+91")){
                phoneNumber1 = "91"+phoneNumber1;
            }
        }


        if (!TextUtils.isEmpty(phoneNumber2) && !phoneNumber2.startsWith("91")) {
            if (!phoneNumber2.startsWith("+91")) {
                phoneNumber2 = "91"+phoneNumber2;
            }
        }

        LoadingDialog.open(this);
        mPresenter.commitPersonalInfoToServer(id, Long.parseLong(memberId), serialNumber, imeiIsSame,
                brandModel, personalAge, sex, applyTime,
                income, maritail,profession,
                education, phoneNumber1, phoneNumber2,
                contact1, contact2, email, phoneTelNumber, language, religion);


        appsflyerEvent(getApp().getPhoneNum(), "COMMIT_PERSONAL_INFO");
        setFirebaseEvent("", "COMMIT_PERSONAL_INFO");

        Bundle bundle = new Bundle();
        setFaceBookEvent(bundle, "COMMIT_PERSONAL_INFO");
    }

    private String getUniqueID(Context context) {
        String id = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
            try {
                UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                id = uuid.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getUUID();
        }

        return TextUtils.isEmpty(id) ? UUID.randomUUID().toString() : id;
    }

    private String getUUID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                ((null != Build.CPU_ABI) ? Build.CPU_ABI.length() : 0) % 10 +

                Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 +

                Build.HOST.length() % 10 + Build.ID.length() % 10 +

                Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 +

                Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 +

                Build.TYPE.length() % 10 + Build.USER.length() % 10; //13 位

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return "8765421120112";
                    }
                    serial = android.os.Build.getSerial();
                } else {
                    serial = Build.SERIAL;
                }
                //API>=9 使用serial号
                return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            } catch (Exception exception) {
                serial = "serial"; // 随便一个初始化
            }
        } else {
            serial = android.os.Build.UNKNOWN; // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    @SetView(R.id.personal_info_relationship1_et)
    private TextView personal_info_relationship1;
    @SetView(R.id.personal_info_relationship2_et)
    private TextView personal_info_relationship2;

    private String name1;
    private String name2;
    private String phone1;
    private String phone2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)
        {
            ToastUtil.show(this, "Failed to obtain contact information");
            return;
        }
        String[] contacts = getPhoneContacts(data.getData());
        String name = contacts[0];
        String phone = contacts[1];
        if(!TextUtils.isEmpty(phone)){
            phone = phone.replace(" ", "").replace("(", "")
                    .replace(")", "").replace("-", "");
        }

        switch (requestCode)
        {
            case 1:
                personal_info_relationship1.setText(name+" - "+phone);
                name1 = name;
                phone1 = phone;
                break;
            case 2:
                personal_info_relationship2.setText(name+" - "+phone);
                name2 = name;
                phone2 = phone;
                break;
        }
    }

    private String[] getPhoneContacts(Uri uri){
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor=cr.query(uri,null,null,null,null);
        if(cursor!=null)
        {
            if (!cursor.moveToNext()) return contact;
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor cursorP = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
            if(cursorP != null){
                if (!cursorP.moveToNext()) return contact;
                contact[1] = cursorP.getString(cursorP.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                cursorP.close();
            }
            cursor.close();
        }
        return contact;
    }

    private void openAddressBook(int requestCode) {
        checkPermission(Manifest.permission.READ_CONTACTS, isGrant -> {
            if (isGrant)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, requestCode);
            }
            else
            {
                Alert.show(this, "Permission denied. Can't get contacts");
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private int profession = -1;
    private int income = -1;
    private int education = -1;
    private int maritail = -1;
    private int religion = -1;
    private String email;
    private String language;

    private void getData(){
        Bundle data = getIntent().getExtras();
        profession = data.getInt("profession");
        income = data.getInt("income");
        education = data.getInt("education");
        religion = data.getInt("religion");
        maritail = data.getInt("maritail");
        email = data.getString("email");
        language = data.getString("language");
    }

    private boolean stringIsEmpty(String str){
        return ("".equals(str) || null == str)?true:false;
    }

    @Override
    public void onGetConfig(BigDataConfig config) {

    }

    @Override
    public void submitCreditSuccess() {

    }

    @Override
    public void onUploadSuccess() {

    }

    @Override
    public void commitPersonInfoSuccess() {
        startActivity(new Intent(PersonalDetailActivity2.this, OCRActivity.class));
        finish();
    }

    @Override
    public void startCrawPhoneData() {

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.show(this, msg);
    }
}