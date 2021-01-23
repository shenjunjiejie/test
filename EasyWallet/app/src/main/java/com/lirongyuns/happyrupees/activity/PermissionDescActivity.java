package com.lirongyuns.happyrupees.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.SetActivity;

@SetActivity(R.layout.activity_permission_desc)
public class PermissionDescActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click({R.id.permission_desc_yes})
    private void click(View v){
        int id = v.getId();
        switch (id){
            case R.id.permission_desc_yes:
                appsflyerEvent(getApp().getPhoneNum(), "CLICK_NEXT");
                setFirebaseEvent("", "CLICK_NEXT");
                setFaceBookEvent(new Bundle(), "CLICK_NEXT");

                if (!getApp().getLoginStatus()) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
        }
    }
}