package com.sy.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.widget.TextView;

import com.sy.prescription.adapter.MedicalAdapter;
import com.sy.prescription.adapter.UsualTypeAdapter;
import com.sy.prescription.model.MedicalInfo;
import com.sy.prescription.model.UsualType;
import com.sy.prescription.util.ToastUtil;
import com.sy.prescription.view.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsualActivity extends BaseActivity {

    public static final int FLAG_USUAL = 1;
    @BindView(R.id.lv_type)
    XRecyclerView lvType;
    @BindView(R.id.lv_medical)
    XRecyclerView lvMedical;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private List<UsualType> mUsualTypes;
    private UsualTypeAdapter mTypeAdapter;

    private List<MedicalInfo> mMedicalList;
    private MedicalAdapter mMedicalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usual);
        ButterKnife.bind(this);
        initNav("常用列表");
        initUI();
    }

    private void initUI() {
        mUsualTypes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UsualType usualType = new UsualType();
            usualType.typeId = "00000" + i;
            usualType.typeName = "类型" + i;
            mUsualTypes.add(usualType);
        }

        lvType.hasMore(true);
        lvType.setLoadingMoreEnabled(true);
        lvType.setPullRefreshEnabled(true);
        lvType.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
        lvType.setLayoutManager(new GridLayoutManager(this, 1));
        mTypeAdapter = new UsualTypeAdapter(this, mUsualTypes);
        lvType.setAdapter(mTypeAdapter);


        mMedicalList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MedicalInfo orderInfo = new MedicalInfo();
            orderInfo.name = "药品" + i;
            orderInfo.num = 0;
            mMedicalList.add(orderInfo);
        }


        lvMedical.hasMore(true);
        lvMedical.setLoadingMoreEnabled(true);
        lvMedical.setPullRefreshEnabled(true);
        lvMedical.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
        lvMedical.setLayoutManager(new GridLayoutManager(this, 1));
        mMedicalAdapter = new MedicalAdapter(this, mMedicalList);
        lvMedical.setAdapter(mMedicalAdapter);

    }

    public void setTotalNum(int totalNum) {
        tvNum.setText("数量:" + totalNum);
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        if (mMedicalAdapter.getmPositions().size() == 0) {
            ToastUtil.show("未选药品");
            return;
        }
        List<Integer> mPosition = new ArrayList<>();
        ArrayList<MedicalInfo> medicalInfos = new ArrayList<>();
        mPosition.addAll(mMedicalAdapter.getmPositions());
        for (int i = 0; i < mPosition.size(); i++) {
            medicalInfos.add(mMedicalList.get(mPosition.get(i).intValue()));
        }
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("medical_list", medicalInfos);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, UsualActivity.class);
        ((OrderDetailActivity) context).startActivityForResult(intent, FLAG_USUAL);
    }
}
