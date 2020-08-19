package com.xu.sinxiao.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xu.sinxiao.common.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configer.getInstance().init(this);
        Utils.showError("xxx");
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}