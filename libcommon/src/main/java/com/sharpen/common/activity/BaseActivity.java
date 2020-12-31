package com.sharpen.common.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public static BaseActivity baseActivity;//传递给非activity的类使用
    public static Context ctx;//传递给非activity的类使用
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity=this;//传递给非activity的类使用
        ctx=this.getBaseContext();//传递给非activity的类使用
    }
}
