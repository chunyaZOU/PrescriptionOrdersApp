package com.sy.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sy.prescription.adapter.MedicalAdapter;
import com.sy.prescription.adapter.OrderAdapter;
import com.sy.prescription.adapter.UsualTypeAdapter;
import com.sy.prescription.model.MedicalInfo;
import com.sy.prescription.model.OrderInfo;
import com.sy.prescription.model.UsualType;
import com.sy.prescription.util.ToastUtil;
import com.ygs.pullrefreshloadmore.PullRefreshLoadMore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsualActivity extends BaseActivity {

    public static final int FLAG_USUAL = 1;
    @BindView(R.id.lv_type)
    PullRefreshLoadMore lvType;
    @BindView(R.id.lv_medical)
    PullRefreshLoadMore lvMedical;
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
        mTypeAdapter = new UsualTypeAdapter(this, mUsualTypes);
        lvType.setAdapter(mTypeAdapter);
        lvType.setOnRefreshListener(new PullRefreshLoadMore.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lvType.onRefreshComplete();
            }
        });
        lvType.setOnLoadListener(new PullRefreshLoadMore.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                lvType.onLoadMoreComplete();
            }
        });
        lvType.setCanLoadMore(true);
        lvType.setCanRefresh(false);
        lvType.setAutoLoadMore(true);
        // 是否自动载入第一页
        lvType.setDoLoadOnUIChanged();


        mMedicalList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MedicalInfo orderInfo = new MedicalInfo();
            orderInfo.name = "药品" + i;
            orderInfo.num = 0;
            mMedicalList.add(orderInfo);
        }

        mMedicalAdapter = new MedicalAdapter(this, mMedicalList);
        lvMedical.setAdapter(mMedicalAdapter);
        lvMedical.setOnRefreshListener(new PullRefreshLoadMore.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lvMedical.onRefreshComplete();
            }
        });
        lvMedical.setOnLoadListener(new PullRefreshLoadMore.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                lvMedical.onLoadMoreComplete();
            }
        });
        lvMedical.setCanLoadMore(true);
        lvMedical.setCanRefresh(false);
        lvMedical.setAutoLoadMore(true);
        // 是否自动载入第一页
        lvMedical.setDoLoadOnUIChanged();

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
