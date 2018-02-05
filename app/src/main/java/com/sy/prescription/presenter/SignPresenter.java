package com.sy.prescription.presenter;

import com.sy.prescription.model.SignInInfo;
import com.sy.prescription.util.GsonUtil;
import com.sy.prescription.util.HttpRequest;
import com.sy.prescription.util.PreferenceUtil;
import com.sy.prescription.util.Urls;
import com.sy.prescription.view.SignView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcy on 2017/3/9.
 */

public class SignPresenter {
    private SignView view;

    public SignPresenter(SignView view) {
        this.view = view;
    }

    public void login(String account, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("account_name", account);
        params.put("account_password", pwd);
        HttpRequest.HttpPost(Urls.API_URL, Urls.API_VERSION_1, Urls.Method.SIGN, Urls.MethodType.POST, params, httpCallback);
        view.showLoading("正在登录");
    }

    private HttpRequest.HttpCallback httpCallback = new HttpRequest.HttpCallback() {
        @Override
        public void httpSuccess(String response) {
            SignInInfo signInInfo = GsonUtil.json2T(response, SignInInfo.class);
            PreferenceUtil.setAuthorization(signInInfo.getData().getSess_id());
            PreferenceUtil.setMerchantShortName(signInInfo.getData().getAccount_name());
            view.onSuccess();
            view.enableBtn();
            view.hideLoading();
        }

        @Override
        public void httpFail(String response) {
            view.enableBtn();
            view.hideLoading();
        }
    };
}