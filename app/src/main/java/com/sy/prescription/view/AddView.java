package com.sy.prescription.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sy.prescription.R;


/**
 * Created by zcy on 2018/2/6.
 */

public class AddView extends LinearLayout implements View.OnClickListener {

    private TextView mTvAdd, mTvReduce, mTvNum;
    private ChangeListener mListener;
    private int mNum;

    public AddView(Context context) {
        super(context);
        init(context);
    }

    public AddView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.view_add, null);
        mTvReduce = (TextView) view.findViewById(R.id.tv_remove);
        mTvAdd = (TextView) view.findViewById(R.id.tv_add);
        mTvNum = (TextView) view.findViewById(R.id.et_num);
        mTvAdd.setOnClickListener(this);
        mTvReduce.setOnClickListener(this);
        addView(view);
    }


    public int getmNum() {
        mNum = Integer.parseInt(mTvNum.getText().toString());
        return mNum;
    }

    public void setmNum(int mNum) {
        mTvNum.setText(mNum + "");
    }

    @Override
    public void onClick(View v) {

        int num = getmNum();
        switch (v.getId()) {
            case R.id.tv_remove:
                num--;
                if (num <= 0) num = 0;
                mTvNum.setText(num + "");
                mListener.setNum(Integer.parseInt(AddView.this.getTag().toString()), num);
                break;
            case R.id.tv_add:
                num++;
                mTvNum.setText(num + "");
                mListener.setNum(Integer.parseInt(AddView.this.getTag().toString()), num);
                break;
        }
    }

    public ChangeListener getmListener() {
        return mListener;
    }

    public void setmListener(ChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface ChangeListener {
        void setNum(int position, int num);
    }

}