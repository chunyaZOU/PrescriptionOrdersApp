package com.sy.prescription;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_welcom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navToMain();
            }
        }, 3000);
    }

    public void navToMain() {
        MainActivity.startAct(this);
        finish();
    }
}