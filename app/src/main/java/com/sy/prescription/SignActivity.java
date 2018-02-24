package com.sy.prescription;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.sy.prescription.presenter.SignPresenter;
import com.sy.prescription.util.PreferenceUtil;
import com.sy.prescription.util.ToastUtil;
import com.sy.prescription.view.SignView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignActivity extends BaseActivity implements SignView {


    public static final int MAIN_REQUEST_CODE = 100;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    private SignPresenter loginPresenter;
    private String mAccount, mPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initNav("登录");
        loginPresenter = new SignPresenter(this);
        etAccount.setText(PreferenceUtil.getUserName());
        etPwd.setText(PreferenceUtil.getPwd());
    }

    @Override
    public void onSuccess() {
        PreferenceUtil.setUserName(mAccount);
        PreferenceUtil.setPwd(mPwd);
        MainActivity.startAct(this);
        finish();
    }

    @Override
    public void enableBtn() {
        btnLogin.setEnabled(true);
    }

    @Override
    public void showLoading(String desc) {
        showToastAnim(desc);
    }

    @Override
    public void hideLoading() {
        hideToastAnim();
    }

    @OnClick(R.id.tvLeft)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.btnLogin)
    public void onBtnLoginClicked() {
        mAccount = etAccount.getText().toString().trim();
        mPwd = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(mAccount)) {
            ToastUtil.show("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(mPwd)) {
            ToastUtil.show("请输入密码");
            return;
        }
        btnLogin.setEnabled(false);
        loginPresenter.login(mAccount, mPwd);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SignActivity.class);
        ((Activity) context).startActivityForResult(intent, MAIN_REQUEST_CODE);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
    }
}