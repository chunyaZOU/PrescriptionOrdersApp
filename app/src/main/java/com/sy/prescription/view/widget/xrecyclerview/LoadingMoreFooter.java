package com.sy.prescription.view.widget.xrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sy.prescription.R;
import com.sy.prescription.util.ResourceUtil;


public class LoadingMoreFooter extends LinearLayout {

    private ProgressBar progressCon;
    private Context mContext;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NO_MORE = 2;
    private TextView mText;

    public LoadingMoreFooter(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        mContext = context;
        setGravity(Gravity.CENTER);

        int margin = ResourceUtil.getDimen(R.dimen.space_10);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        progressCon = new ProgressBar(context);
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(margin * 2, margin * 2));
        progressCon.setIndeterminateDrawable(getResources().getDrawable(R.drawable.xrecycler_footer_loading_progress));
        addView(progressCon);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, margin, 0, margin);
        mText = new TextView(context);
        mText.setText(getResources().getString(R.string.foot_loading));
        mText.setTextColor(ResourceUtil.getColor(R.color.app_theme_gray));
        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mText.setText(mContext.getText(R.string.foot_loading));
                progressCon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(mContext.getText(R.string.foot_loading));
                setVisibility(View.GONE);
                break;
            case STATE_NO_MORE:
                mText.setText(mContext.getText(R.string.no_more_loading));
                progressCon.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                break;
        }
    }
}
