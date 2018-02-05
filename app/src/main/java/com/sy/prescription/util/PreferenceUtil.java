package com.sy.prescription.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sy.prescription.config.IMApplication;

/**
 * Created by ygs on 2017/3/15.
 */

public class PreferenceUtil {

    private static SharedPreferences preferences = null;

    private static SharedPreferences getPreferences() {
        if (preferences == null) {
            return PreferenceManager.getDefaultSharedPreferences(IMApplication.getAppContext());
        }
        return preferences;
    }

    public static void setAuthorization(String authorization) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("sess_id", authorization);
        editor.commit();
    }

    public static String getAuthorization() {
        return getPreferences().getString("sess_id", "");
    }

    public static void setToken(String token) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static String getToken() {
        return getPreferences().getString("token", "");
    }

    public static void setSignInInfo(String signInInfo) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("signInInfo", signInInfo);
        editor.commit();
    }

    public static String getSignInInfo() {
        return getPreferences().getString("signInInfo", "");
    }

    public static void setUserName(String userName) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("userName", userName);
        editor.commit();
    }

    public static String getUserName() {
        return getPreferences().getString("userName", "");
    }

    public static void setPwd(String pwd) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("password", pwd);
        editor.commit();
    }

    public static String getPwd() {
        return getPreferences().getString("password", "");
    }


    public static String getMerchantShortName() {
        return getPreferences().getString("MerchantShortName", "");
    }

    public static void setMerchantShortName(String merchantShortName) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("MerchantShortName", merchantShortName);
        editor.commit();
    }
}
