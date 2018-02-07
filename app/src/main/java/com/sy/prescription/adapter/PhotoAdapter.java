package com.sy.prescription.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sy.prescription.MainActivity;
import com.sy.prescription.R;
import com.sy.prescription.fragment.PrescriptionFragment;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ygs on 2017/3/11.
 */

public class PhotoAdapter extends BaseAdapter {
    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater mInflate;

    public PhotoAdapter(List<String> data, Context context) {
        mDatas = data;
        this.mContext = context;
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflate.inflate(R.layout.view_photo_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mDatas.get(i).equals(PrescriptionFragment.TAKE_PHOTO)) {
            holder.tvTakePhoto.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.GONE);
        } else {
            holder.tvTakePhoto.setVisibility(View.GONE);
            holder.img.setVisibility(View.VISIBLE);
            if (mDatas.get(i).contains("http")) {
                holder.img.setImageURI(Uri.parse(mDatas.get(i)));
            } else {
                holder.img.setImageURI(Uri.fromFile(new File(mDatas.get(i))));
            }
        }
        holder.tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PrescriptionFragment) ((MainActivity) mContext).getCurrentFragment()).chooseHeadImage();
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.img)
        SimpleDraweeView img;
        @BindView(R.id.tv_take_photo)
        TextView tvTakePhoto;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}