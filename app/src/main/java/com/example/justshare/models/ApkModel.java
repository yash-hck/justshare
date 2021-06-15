package com.example.justshare.models;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ApkModel {
    private String apkPath;
    private String title;
    private String packageName;
    private String versionName;
    private Drawable icon;
    private int versionCode = 0;
    private boolean isSelected;


    public void printInfo(){
        Log.d("data is here lelo",title + "\t" + packageName + "\t" + versionName + "\t" + versionCode);
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
