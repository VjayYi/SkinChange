package com.dongnao.resouce_update;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;

public class BaseActivity extends Activity {
    SkinFactory skinFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().setContext(this);
        skinFactory = new SkinFactory();
        LayoutInflaterCompat.setFactory2(getLayoutInflater(),skinFactory);
    }

    public void apply(){
        skinFactory.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        skinFactory.apply();
    }
}
