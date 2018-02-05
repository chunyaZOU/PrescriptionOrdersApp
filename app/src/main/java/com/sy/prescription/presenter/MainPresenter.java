package com.sy.prescription.presenter;

import android.view.View;
import android.widget.TextView;

import com.sy.prescription.R;
import com.sy.prescription.config.IMApplication;
import com.sy.prescription.util.HttpRequest;
import com.sy.prescription.util.PreferenceUtil;
import com.sy.prescription.view.MainView;

/**
 * Created by zcy on 2017/3/9.
 */

public class MainPresenter {
    private MainView view;
    private boolean isAllExit;

    public MainPresenter(MainView view) {
        this.view = view;
    }

}