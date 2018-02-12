package com.sy.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.sy.prescription.adapter.MedicalAdapter;
import com.sy.prescription.adapter.UsualTypeAdapter;
import com.sy.prescription.model.MedicalInfo;
import com.sy.prescription.model.UsualType;
import com.ygs.pullrefreshloadmore.PullRefreshLoadMore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {

    @BindView(R.id.lv_type)
    PullRefreshLoadMore lvType;
    @BindView(R.id.lv_medical)
    PullRefreshLoadMore lvMedical;


    private List<UsualType> mUsualTypes;
    private UsualTypeAdapter mTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
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


        lvMedical.setAdapter(mTypeAdapter);
        lvType.setAdapter(mTypeAdapter);


    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }
}
