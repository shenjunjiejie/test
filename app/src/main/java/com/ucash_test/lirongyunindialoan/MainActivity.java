package com.ucash_test.lirongyunindialoan;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ucash_test.lirongyunindialoan.activity.BasicActivity;
import com.ucash_test.lirongyunindialoan.annonation.Click;
import com.ucash_test.lirongyunindialoan.annonation.SetActivity;
import com.ucash_test.lirongyunindialoan.annonation.SetView;
import com.ucash_test.lirongyunindialoan.annonation.Title;

@SetActivity(R.layout.activity_main)
@Title(value = {R.layout.title,R.id.title_tv,R.id.title_iv_back,R.id.bg_title,R.id.title_fl_content},
        title = "main",textColor = Color.YELLOW,
        bgcolor = Color.GRAY,backColor = Color.WHITE)
public class MainActivity extends BasicActivity {
    @SetView(R.id.test)
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv.setText("aaa");
    }

    @Click(R.id.test)
    private void click(View view){
        Toast.makeText(this,"test",Toast.LENGTH_LONG).show();
    }



}