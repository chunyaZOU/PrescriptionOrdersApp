package com.sy.prescription;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sy.prescription.adapter.BasePagerAdapter;
import com.sy.prescription.fragment.BaseFragment;
import com.sy.prescription.fragment.PhotoFragment;
import com.sy.prescription.fragment.PrescriptionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BaseFragment.OnFragmentInteractionListener {

    @BindView(R.id.vp)
    ViewPager vp;
    private List<Fragment> mfragments;
    private List<String> mPath;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        initNav("图片");
        initFragments();
    }


    private void initFragments() {
        pos = getIntent().getIntExtra("pos", 0);
        mPath = new ArrayList<>();
        mPath.addAll(getIntent().getStringArrayListExtra("paths"));
        mfragments = new ArrayList<>();
        for (int i = 0; i < mPath.size(); i++) {
            if(!mPath.get(i).equals(PrescriptionFragment.TAKE_PHOTO)){
                mfragments.add(PhotoFragment.newInstance(mPath.get(i)));
            }
        }
        vp.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), mfragments));
        vp.addOnPageChangeListener(this);
        vp.setCurrentItem(pos);
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

    public static void startAct(Context context, ArrayList<String> paths, int pos) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putStringArrayListExtra("paths", paths);
        intent.putExtra("pos", pos);
        context.startActivity(intent);
    }

}