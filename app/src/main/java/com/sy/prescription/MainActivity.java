package com.sy.prescription;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sy.prescription.adapter.BasePagerAdapter;
import com.sy.prescription.fragment.BaseFragment;
import com.sy.prescription.fragment.PrescriptionFragment;
import com.sy.prescription.fragment.SearchFragment;
import com.sy.prescription.util.ImgPickerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener,BaseFragment.OnFragmentInteractionListener {


    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vp)
    ViewPager vp;
    private List<Fragment> mfragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImgPickerUtil.initImagePicker(this);
        initFragments();
        setupViewPager();
        initTabs();
    }

    private void initFragments() {
        mfragments = new ArrayList<>();
        mfragments.add(PrescriptionFragment.newInstance("home", "home"));
        mfragments.add(SearchFragment.newInstance("study", "study"));
    }

    private void setupViewPager() {
        vp.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), mfragments));
        vp.addOnPageChangeListener(this);
        onPageSelected(0);
        tab.setupWithViewPager(vp, true);
    }

    private void initTabs() {
        String[] titles = getResources().getStringArray(R.array.nav_titles);
        for (int i = 0; i < mfragments.size(); i++) {
            tab.getTabAt(i).setText(titles[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}