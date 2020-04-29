package com.dongnao.loadres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dongnao.resouce_update.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeSkin(View view) {
        Intent intent = new Intent().setClass(MainActivity.this,TwoActivity.class);
        startActivity(intent);
    }
}
