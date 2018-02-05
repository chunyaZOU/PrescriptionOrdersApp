package com.sy.prescription.view;

/**
 * Created by zcy on 2017/3/9.
 */

public interface MainView {


    //跳转登录
    void navToLogin();

    //签退
    void signOutIM();

    //签退
    void signOutSuc();

    void signOutErr(int i, String s);

    //跳转微信收款
    void navToWechat();

    //跳转支付宝收款
    void navToAlipay();

    //跳转现金收款
    void navToCash();

    //跳转刷卡收款
    void navToCard();

    //跳转申请退款
    void navToRefund();

    //跳转订单流水
    void navToForm();

    //跳转数据统计
    void navToDataCount();

    //跳转终端管理
    void navToDevice();

    //跳转打印管理
    void navToPrintManage();

    //跳转订单号查询
    void navToOrderSelect();

    //跳转预留
    void navToReserve();
}
