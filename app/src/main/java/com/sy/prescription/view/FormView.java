package com.sy.prescription.view;

/**
 * Created by ygs on 2017/3/11.
 */

public interface FormView {
    void setTitleForm();

    void setTitlePay();

    void showPop();

    void hidePop();

    void selectConfirm(String payWayTags, String payStatusTags);

    void setTotalInfo(String TotalFee, String TotalCount);
}
