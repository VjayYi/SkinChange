package com.dongnao.resouce_update;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class SkinFactory implements LayoutInflater.Factory2 {
    private static final String[] prxfixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    //这个容器 收集需要换肤的控件
    private List<SkinView> viewList = new ArrayList<>();

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        //能够判断每个控件是否需要换肤 同时 能够再这里面给它进行换肤
        View view = null;
        if(name.contains(".")){
            //有包名
            view = onCreateView(name,context,attrs);
        }else {
            //没有包名 去拼包名
            for (String s : prxfixList) {
                String viewName = s+name;
                view = onCreateView(viewName,context,attrs);
                if(view!=null){
                    break;
                }
            }
        }
        if(view!=null){
            paserView(view,name,attrs);
        }
        return view;
    }

    public void apply(){
        for (SkinView skinView : viewList) {
            skinView.apply();
        }
    }


    private void paserView(View view, String name, AttributeSet attrs) {
        List<SkinItem> skinItems = new ArrayList<>();
        //遍历所有的属性  找到需要换肤的属性
        for(int x=0;x<attrs.getAttributeCount();x++){
            String attributeName = attrs.getAttributeName(x);
            if(attributeName.contains("background") || attributeName.contains("textColor")
            ||attributeName.contains("src")){
                //获取ID
                String attributeValue = attrs.getAttributeValue(x);
                int resId = Integer.parseInt(attributeValue.substring(1));
                //获取到资源id的类型
                String resourceTypeName = view.getResources().getResourceTypeName(resId);
                //获取到的就是资源id的名字
                String resourceEntryName = view.getResources().getResourceEntryName(resId);
                SkinItem skinItem = new SkinItem(attributeName,resourceTypeName,resourceEntryName,resId);
                skinItems.add(skinItem);
            }
        }
        if(skinItems.size()>0){
            SkinView skinView = new SkinView(view,skinItems);
            viewList.add(skinView);
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            Class aClass = context.getClassLoader().loadClass(name);
            Constructor<? extends View> constructor = aClass.getConstructor(Context.class, AttributeSet.class);
            //执行构造方法
            view = constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    class SkinView{
        View view;
        List<SkinItem> itemList;

        public SkinView(View view, List<SkinItem> itemList) {
            this.view = view;
            this.itemList = itemList;
        }

        public void apply(){
            for (SkinItem skinItem : itemList) {
                if(skinItem.getName().equals("background")){
                    //1. 设置的是color颜色  2.设置的是图片
                    if(skinItem.getTypeName().equals("color")){
                        //将资源ID  传给ResouceManager  去进行资源匹配   如果匹配到了  就直接设置给控件
                        // 如果没有匹配到  就把之前的资源ID  设置控件
                        view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }else if(skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")){
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                            view.setBackground(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }else{
                            view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                }else if(skinItem.getName().equals("src")){
                    if(skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")){
                        //将资源ID  传给ResouceManager  去进行资源匹配   如果匹配到了  就直接设置给控件
                        // 如果没有匹配到  就把之前的资源ID  设置控件
                        ((ImageView)view).setImageResource(SkinManager.getInstance().getDrawableId(skinItem.getResId()));
                    }
                }else if(skinItem.getName().equals("textColor")){
                    ((TextView)view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                }
            }
        }
    }


    /**
     * 每条属性的封装对象
     */
    class SkinItem{
        //属性的名字  textColor text background
        String name;
        //属性的值的类型 color mipmap
        String typeName;
        //属性的值的名字  colorPrimary
        String entryName;
        //属性的资源ID
        int resId;


        public SkinItem(String name, String typeName, String entryName, int resId) {
            this.name = name;
            this.typeName = typeName;
            this.entryName = entryName;
            this.resId = resId;
        }

        public String getName() {
            return name;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getEntryName() {
            return entryName;
        }

        public int getResId() {
            return resId;
        }
    }
}
