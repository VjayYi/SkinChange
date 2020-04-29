package com.dongnao.loadres;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.dongnao.resouce_update.BaseActivity;
import com.dongnao.resouce_update.SkinManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class TwoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }

    public String loadDex(Context context, String name) {
        File fileDir = context.getDir("dex", Context.MODE_PRIVATE);
        String dexPath = fileDir.getAbsoluteFile() + File.separator + name;
        File dexFile = new File(dexPath);
        if (!dexFile.exists()) {
            try {
                dexFile.createNewFile();
                copyFiles(context, name, dexFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  dexPath;
    }

    public static void copyFiles(Context context, String fileName, File desFile) {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = context.getAssets().open(fileName);
            out = new FileOutputStream(desFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = in.read(bytes)) != -1)
                out.write(bytes, 0, len);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeSkin(View view) {
        String str=loadDex(TwoActivity.this,"skin-debug.apk");
        //去加载资源包
        SkinManager.getInstance().loadResouceApk(str);
        //换肤
        apply();
    }
}
