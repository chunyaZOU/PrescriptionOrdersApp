package com.sy.prescription.presenter;

import android.os.Handler;

import com.sy.prescription.view.WelcomeView;

/**
 * Created by zcy on 2017/3/9.
 */

public class WelcomePresenter {

    private WelcomeView view;

    public WelcomePresenter(WelcomeView view) {
        this.view=view;
    }

    public void navToMain(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.navToMain();
            }
        }, 3000);
    }
}
