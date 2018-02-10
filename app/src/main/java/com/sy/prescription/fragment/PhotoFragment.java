package com.sy.prescription.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.sy.prescription.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by zcy on 2018/2/10.
 */

public class PhotoFragment extends BaseFragment {

    private String mPath;

    public PhotoFragment() {
        // Required empty public constructor
    }

    public static PhotoFragment newInstance(String path) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPath = getArguments().getString("path");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ViewHolder holder = new ViewHolder(view);
        if (mPath.contains("http")) {
            Glide.with(this).load(mPath).into(holder.photoView);
        } else {
            Glide.with(this).load(new File(mPath)).into(holder.photoView);
        }
        holder.photoView.canZoom();
        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    static class ViewHolder {
        @BindView(R.id.photo_view)
        PhotoView photoView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
