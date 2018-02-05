package com.sy.prescription.util;

import android.widget.Toast;

import com.sy.prescription.config.IMApplication;

/**
 * Created by zcy on 2017/3/9.
 */

public class ToastUtil {

    public static void show(int resId){
        Toast.makeText(IMApplication.getAppContext(), IMApplication.getAppContext().getString(resId), Toast.LENGTH_SHORT).show();
    }
    public static void show(String desc){
        Toast.makeText(IMApplication.getAppContext(), desc, Toast.LENGTH_SHORT).show();
    }

}
