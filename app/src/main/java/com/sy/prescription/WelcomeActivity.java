package com.sy.prescription;

import android.os.Bundle;
import android.view.WindowManager;

import com.sy.prescription.presenter.WelcomePresenter;
import com.sy.prescription.view.WelcomeView;

public class WelcomeActivity extends BaseActivity implements WelcomeView {

    private WelcomePresenter welcomePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_welcom);
        welcomePresenter=new WelcomePresenter(this);
        welcomePresenter.navToMain();
    }

    @Override
    public void navToMain() {
        MainActivity.startAct(this);
        finish();
    }

}