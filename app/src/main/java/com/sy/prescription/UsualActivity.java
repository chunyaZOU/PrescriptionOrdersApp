package com.sy.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ygs.pullrefreshloadmore.PullRefreshLoadMore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsualActivity extends BaseActivity {

    @BindView(R.id.lv_type)
    PullRefreshLoadMore lvType;
    @BindView(R.id.lv_medical)
    PullRefreshLoadMore lvMedical;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usual);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
    }

    public static void startAct(Context context){
        Intent intent=new Intent(context,UsualActivity.class);
        context.startActivity(intent);
    }
}
