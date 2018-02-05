package com.sy.prescription.view;

import android.view.View;

/**
 * Created by zcy on 2017/3/10.
 */

public interface RefundView{

    void showPopWindow(View v);
    void resetPopWindow(View v);
    void pay();
    void confirm();
    void cashCalculate();
}
