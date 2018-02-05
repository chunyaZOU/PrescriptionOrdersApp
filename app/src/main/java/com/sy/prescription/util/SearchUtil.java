package com.sy.prescription.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sy.prescription.config.IMApplication;

/**
 * Created by ygs on 2017/3/15.
 */

public class SearchUtil {

    private static SharedPreferences preferences = null;

    private static SharedPreferences getPreferences() {
        if (preferences == null) {
            return PreferenceManager.getDefaultSharedPreferences(IMApplication.getAppContext());
        }
        return preferences;
    }

    public static void setStorageSn(String pwd) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("storage_sn", pwd);
        editor.commit();
    }

    public static String getStorageSn() {
        return getPreferences().getString("storage_sn", "");
    }

}
