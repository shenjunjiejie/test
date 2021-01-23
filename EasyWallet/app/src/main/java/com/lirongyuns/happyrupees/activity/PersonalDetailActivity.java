package com.lirongyuns.happyrupees.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;
import com.lirongyuns.happyrupees.utils.CheckUtil;
import com.lirongyuns.happyrupees.utils.ToastUtil;

@SetActivity(R.layout.activity_peronnal_detail)
public class PersonalDetailActivity extends BaseActivity {

    @SetView(R.id.personal_info_email)
    private EditText personal_info_email;
    @SetView(R.id.personal_info_language)
    private EditText personal_info_language;
    @SetView(R.id.personal_info_profession_et)
    private TextView personal_info_profession_et;
    @SetView(R.id.personal_info_income_et)
    private TextView personal_info_income_et;
    @SetView(R.id.personal_info_education_et)
    private TextView personal_info_education_et;
    @SetView(R.id.personal_info_maritail_et)
    private TextView personal_info_maritail_et;
    @SetView(R.id.personal_info_religion_et)
    private TextView personal_info_religion_et;


    private PopupMenu professionMenu;
    private PopupMenu incomeMenu;
    private PopupMenu educationMenu;
    private PopupMenu maritailMenu;
    private PopupMenu religionMenu;

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        religionMenu = new PopupMenu(this, personal_info_religion_et);
        religionMenu.inflate(R.menu.religion);
        religionMenu.setOnMenuItemClickListener(item -> {
            personal_info_religion_et.setText(item.getTitle());
            religion = Integer.parseInt((String) item.getTitleCondensed());
            return true;
        });
        professionMenu = new PopupMenu(this, personal_info_profession_et);
        professionMenu.inflate(R.menu.profession);
        professionMenu.setOnMenuItemClickListener(item -> {
            personal_info_profession_et.setText(item.getTitle());
            profession = Integer.parseInt((String) item.getTitleCondensed());
            return true;
        });

        incomeMenu = new PopupMenu(this, personal_info_income_et);
        incomeMenu.inflate(R.menu.income);
        incomeMenu.setOnMenuItemClickListener(item -> {
            personal_info_income_et.setText(item.getTitle());
            income = Integer.parseInt((String) item.getTitleCondensed());
            return true;
        });
        educationMenu = new PopupMenu(this, personal_info_education_et);
        educationMenu.inflate(R.menu.education);
        educationMenu.setOnMenuItemClickListener(item -> {
            personal_info_education_et.setText(item.getTitle());
            education = Integer.parseInt((String) item.getTitleCondensed());
            return true;
        });
        maritailMenu = new PopupMenu(this, personal_info_maritail_et);
        maritailMenu.inflate(R.menu.maritail);
        maritailMenu.setOnMenuItemClickListener(item -> {
            personal_info_maritail_et.setText(item.getTitle());
            maritail = Integer.parseInt((String) item.getTitleCondensed());
            return true;
        });
    }


    @Click({R.id.personal_info_profession,R.id.personal_info_profession_et,
            R.id.personal_info_income,R.id.personal_info_income_et,
            R.id.personal_info_education_et,R.id.personal_info_education,
            R.id.personal_info_religion,R.id.personal_info_religion_et,
            R.id.personal_info_maritail_et,R.id.personal_info_maritail,
            R.id.personal_info_next})
    private void click(View view){
        switch (view.getId()){
            case R.id.personal_info_profession:
            case R.id.personal_info_profession_et:
                professionMenu.show();
                break;
            case R.id.personal_info_income:
            case R.id.personal_info_income_et:
                incomeMenu.show();
                break;
            case R.id.personal_info_education:
            case R.id.personal_info_education_et:
                educationMenu.show();
                break;
            case R.id.personal_info_religion:
            case R.id.personal_info_religion_et:
                religionMenu.show();
                break;
            case R.id.personal_info_maritail_et:
            case R.id.personal_info_maritail:
                maritailMenu.show();
                break;
            case R.id.personal_info_next:
                email = personal_info_email.getText().toString();
                language = personal_info_language.getText().toString();
                if(stringIsEmpty(email)){
                    ToastUtil.show(this,"email is empty.");
                    break;
                }
                if (!CheckUtil.isEmail(email)) {
                    ToastUtil.show(this, "email is not correct.");
                    return;
                }
                else if(stringIsEmpty(language)){
                    ToastUtil.show(this,"language is empty.");
                    break;
                }
                else if(intIsUnable(religion)){
                    ToastUtil.show(this,"religion is empty.");
                    break;
                }
                else if(intIsUnable(profession)){
                    ToastUtil.show(this,"profession is empty.");
                    break;
                }
                else if(intIsUnable(income)){
                    ToastUtil.show(this,"income is empty.");
                    break;
                }
                else if(intIsUnable(education)){
                    ToastUtil.show(this,"education is empty.");
                    break;
                }
                else if(intIsUnable(maritail)){
                    ToastUtil.show(this,"maritail is empty.");
                    break;
                }
                Intent intent = new Intent(PersonalDetailActivity.this, PersonalDetailActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("language",language);
                bundle.putInt("profession",profession);
                bundle.putInt("income",income);
                bundle.putInt("education",education);
                bundle.putInt("maritail",maritail);
                bundle.putInt("religion",religion);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private int profession = -1;
    private int income = -1;
    private int education = -1;
    private int maritail = -1;
    private int religion = -1;
    private String email;
    private String language;

    private boolean stringIsEmpty(String str){
        return ("".equals(str) || null == str)?true:false;
    }
    private boolean intIsUnable(int num){
        return (num < 0)?true:false;
    }
}