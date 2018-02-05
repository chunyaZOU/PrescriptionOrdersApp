package com.sy.prescription.config;

import android.app.Application;
import android.content.Context;

import com.sy.prescription.util.FrescoHelper;
import com.sy.prescription.util.ResourceUtil;
import com.sy.prescription.util.StoragePathHelper;
import com.sy.prescription.util.StoragePathManager;


/**
 * Created by zcy on 2017/3/8.
 */

public class IMApplication extends Application {
    private static boolean isLogin;
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        //初始化存储
        StoragePathHelper.init(getApplicationContext());
        //初始化缓存
        StoragePathManager.init(getApplicationContext());
        //Fresoc 初始化
        FrescoHelper.initFresco(getApplicationContext());
        ResourceUtil.init(getApplicationContext());
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        IMApplication.isLogin = isLogin;
    }

    public static Context getAppContext(){
        return mContext;
    }
}
