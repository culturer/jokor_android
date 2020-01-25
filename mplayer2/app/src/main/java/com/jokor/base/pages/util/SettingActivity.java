package com.jokor.base.pages.util;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jokor.base.R;

//设置界面
//1.是否自动登录
//2.电话号码可见。对所有人可见，对亲人可见，不可见

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
}
