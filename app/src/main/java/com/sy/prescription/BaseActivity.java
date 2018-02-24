package com.sy.prescription;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity {
    private Context mContext;
    // 加载的动画效果
    private Dialog loadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.mContext = this;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_left);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showToastAnim(String... hint) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);
        if (loadView == null) {
            loadView = new Dialog(this, R.style.CustomDialog);
            loadView.setContentView(R.layout.view_loading_anim);
            loadView.setCancelable(true);
            loadView.setCanceledOnTouchOutside(false);
            loadView.findViewById(R.id.img_dialog).startAnimation(anim);
            if (null != hint && hint.length > 0 && !TextUtils.isEmpty(hint[0])) {
                ((TextView) loadView.findViewById(R.id.text_hint)).setText(hint[0]);
            }
        }
        if (!loadView.isShowing()) {
            loadView.findViewById(R.id.img_dialog).startAnimation(anim);
            if (null != hint && hint.length > 0 && !TextUtils.isEmpty(hint[0])) {
                ((TextView) loadView.findViewById(R.id.text_hint)).setText(hint[0]);
            }
            loadView.show();
        }
    }

    public void hideToastAnim() {
        if (loadView != null && loadView.isShowing())
            loadView.cancel();
    }

    protected void initNav(int strTitle) {
        initNav();
        findViewById(R.id.tvLeft).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_left);
            }
        });
        ((TextView) this.findViewById(R.id.tvMiddle)).setText(strTitle);
    }

    private void initNav() {
        ((TextView) findViewById(R.id.tvLeft)).setCompoundDrawablesWithIntrinsicBounds(setCompoundImg(R.drawable.back), null, null, null);
        ((TextView) findViewById(R.id.tvRight)).setCompoundDrawablesWithIntrinsicBounds(setCompoundImg(R.drawable.back), null, null, null);
        ((TextView) findViewById(R.id.tvRight)).setVisibility(View.INVISIBLE);
    }

    protected void initNav(String strTitle) {
        initNav();
        this.findViewById(R.id.tvLeft).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_left);
            }
        });
        ((TextView) this.findViewById(R.id.tvMiddle)).setText(strTitle);
    }

    protected Drawable setCompoundImg(int imgId) {
        Drawable nav_up = getResources().getDrawable(imgId);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        return nav_up;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dialog window leak 20160919
        if (loadView != null) {
            loadView.dismiss();
        }
    }
}