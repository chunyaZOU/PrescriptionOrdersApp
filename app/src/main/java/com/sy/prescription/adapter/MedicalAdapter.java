package com.sy.prescription.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.prescription.R;
import com.sy.prescription.model.MedicalInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ygs on 2018/2/3.
 */

public class MedicalAdapter extends BaseAdapter {

    private Context mContext;
    private List<MedicalInfo> mData;
    private LayoutInflater mInflate;

    public MedicalAdapter(Context context, List<MedicalInfo> medicalInfos) {
        mContext = context;
        mData = medicalInfos;
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflate.inflate(R.layout.view_medical_layout, viewGroup);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MedicalInfo orderInfo = mData.get(i);
        holder.tvName.setText(orderInfo.name);
        holder.etNum.setText(orderInfo.num);
        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_remove)
        TextView tvRemove;
        @BindView(R.id.et_num)
        EditText etNum;
        @BindView(R.id.tv_add)
        TextView tvAdd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
