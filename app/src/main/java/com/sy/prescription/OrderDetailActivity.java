package com.sy.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.sy.prescription.adapter.MedicalAdapter;
import com.sy.prescription.model.MedicalInfo;
import com.ygs.pullrefreshloadmore.PullRefreshLoadMore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity implements PullRefreshLoadMore.OnRefreshListener,PullRefreshLoadMore.OnLoadMoreListener {

    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.tv_usual)
    TextView tvUsual;
    @BindView(R.id.lv_medical)
    PullRefreshLoadMore lvMedical;

    private List<MedicalInfo> mMedicalList;
    private MedicalAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI(){
        mMedicalList=new ArrayList<>();
        mAdapter=new MedicalAdapter(this,mMedicalList);
        lvMedical.setAdapter(mAdapter);
    }

    @OnClick(R.id.tv_scan)
    public void onTvScanClicked() {
        CaptureActivity.startAct(this,"",0);
    }

    @OnClick(R.id.tv_usual)
    public void onTvUsualClicked() {
        UsualActivity.startAct(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public static void startAct(Context context){
        Intent intent=new Intent(context,OrderDetailActivity.class);
        context.startActivity(intent);
    }
}
