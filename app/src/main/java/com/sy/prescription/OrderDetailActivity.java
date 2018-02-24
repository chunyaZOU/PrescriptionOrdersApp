package com.sy.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.cloudcommune.yhonline.imgpicker.bean.ImageItem;
import com.sy.prescription.adapter.MedicalAdapter;
import com.sy.prescription.adapter.PhotoAdapter;
import com.sy.prescription.model.MedicalInfo;
import com.sy.prescription.util.ToastUtil;
import com.sy.prescription.view.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OrderDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.tv_usual)
    TextView tvUsual;
    @BindView(R.id.lv_medical)
    XRecyclerView lvMedical;
    @BindView(R.id.tvLeft)
    TextView tvLeft;
    @BindView(R.id.tvMiddle)
    TextView tvMiddle;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private List<MedicalInfo> mMedicalList;
    private MedicalAdapter mAdapter;

    private PhotoAdapter mPhotoAdapter;
    private ArrayList<String> mImgPaths;

    private ArrayList<ImageItem> images = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {

        mImgPaths = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mImgPaths.add("http://a.hiphotos.baidu.com/image/h%3D300/sign=71f6f27f2c7f9e2f6f351b082f31e962/500fd9f9d72a6059f550a1832334349b023bbae3.jpg");
        }
        mPhotoAdapter = new PhotoAdapter(mImgPaths, this);
        gv.setAdapter(mPhotoAdapter);
        gv.setOnItemClickListener(this);


        initNav(getIntent().getStringExtra("title"));

        lvMedical.hasMore(false);
        lvMedical.setPullRefreshEnabled(false);
        lvMedical.setLoadingMoreEnabled(false);
        mMedicalList = new ArrayList<>();
        lvMedical.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new MedicalAdapter(this, mMedicalList);
        lvMedical.setAdapter(mAdapter);
    }

    @OnClick(R.id.tv_scan)
    public void onTvScanClicked() {
        CaptureActivity.startAct(this, "扫描");
        //TestActivity.startAct(this);
    }

    @OnClick(R.id.tv_usual)
    public void onTvUsualClicked() {
        UsualActivity.startAct(this);
    }

    public void setTotalNum(int totalNum) {
        tvNum.setText("数量:" + totalNum);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CaptureActivity.SCAN_FLAG:
                    MedicalInfo info = new MedicalInfo();
                    info.name = data.getStringExtra("num");
                    mMedicalList.add(info);
                    mAdapter.setTotalNum0();
                    break;
                case UsualActivity.FLAG_USUAL:
                    ArrayList<MedicalInfo> medicalInfos = data.getParcelableArrayListExtra("medical_list");
                    mMedicalList.addAll(medicalInfos);
                    mAdapter.setTotalNum0();
                    break;
            }
        }
    }

    @OnClick({R.id.tvLeft, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLeft:
                break;
            case R.id.tv_submit:
                if (mMedicalList.size() == 0) {
                    ToastUtil.show("未选择药品");
                    return;
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhotoActivity.startAct(this, mImgPaths, position);
    }

    public static void startAct(Context context, String cardNum) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("title", cardNum);
        context.startActivity(intent);
    }
}
