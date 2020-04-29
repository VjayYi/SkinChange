package com.dongnao.resouce_update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Method;

/**
 * 加载资源包  从某一个路径下去加载  获取到这个资源包中的资源对象
 */
public class SkinManager {
    private static SkinManager skinManager = new SkinManager();
    private Context context;
    //资源包的资源对象
    private Resources resources;
    //资源包的包名
    private String packageName;


    private SkinManager(){}

    public static SkinManager getInstance(){
        return skinManager;
    }

    public void setContext(Context context){
        this.context = context;
    }

    /**
     * 根据传进来的路径去加载资源包
     * @param path
     */
    public void  loadResouceApk(String path){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path,
                PackageManager.GET_ACTIVITIES);
        packageName = packageArchiveInfo.packageName;
        try {
            //资源的直接管理者
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            //执行addAssetPath方法
            addAssetPath.invoke(assetManager,path);
            resources = new Resources(assetManager,context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *  根据名字和类型去资源包中匹配
     * @param resId 当前APP中的资源ID 123456789
     * @return 匹配到的资源id  这个资源ID  就是在资源包中匹配到的资源的ID
     */
    public int getColor(int resId){
        if(resources == null){
            return resId;
        }
        String resourceTypeName = context.getResources().getResourceTypeName(resId);
        String resourceEntryName = context.getResources().getResourceEntryName(resId);
        //去资源包的资源对象中去匹配
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, packageName);
        if(identifier == 0){
            return resId;
        }
        return resources.getColor(identifier);
    }


    /**
     * 从资源包中拿到drawable的资源id
     */
    public Drawable getDrawable(int id){
        if(resources == null){
            return ContextCompat.getDrawable(context,id);
        }
        //获取到资源id的类型
        String resourceTypeName = context.getResources().getResourceTypeName(id);
        //获取到的就是资源id的名字
        String resourceEntryName = context.getResources().getResourceEntryName(id);
        //就是colorAccent这个资源在外置APK中的id
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, packageName);
        if(identifier == 0){
            return ContextCompat.getDrawable(context,id);
        }
        return resources.getDrawable(identifier);
    }

    /**
     * 从资源包中拿到drawable的资源id
     */
    public int getDrawableId(int id){
        if(resources == null){
            return id;
        }
        //获取到资源id的类型
        String resourceTypeName = context.getResources().getResourceTypeName(id);
        //获取到的就是资源id的名字
        String resourceEntryName = context.getResources().getResourceEntryName(id);
        //就是colorAccent这个资源在外置APK中的id
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, packageName);
        if(identifier == 0){
            return id;
        }
        return identifier;
    }





}
