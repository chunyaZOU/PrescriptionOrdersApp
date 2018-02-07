package com.sy.prescription.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sy.prescription.OrderDetailActivity;
import com.sy.prescription.R;
import com.sy.prescription.UsualActivity;
import com.sy.prescription.model.MedicalInfo;
import com.sy.prescription.view.AddView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ygs on 2018/2/3.
 */

public class MedicalAdapter extends BaseAdapter implements AddView.ChangeListener {

    private Context mContext;
    private List<MedicalInfo> mData;
    private LayoutInflater mInflate;
    private List<Integer> mPositions;
    private int mTotalNum;
    private boolean isFirst = true;

    public MedicalAdapter(Context context, List<MedicalInfo> medicalInfos) {
        mContext = context;
        mData = medicalInfos;
        mInflate = LayoutInflater.from(context);
        mPositions = new ArrayList<>();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflate.inflate(R.layout.view_medical_layout, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        MedicalInfo orderInfo = mData.get(i);
        holder.tvName.setText(orderInfo.name);
        holder.addView.setTag(i + "");
        holder.addView.setmListener(this);
        holder.addView.setmNum(orderInfo.num);
        if (isFirst) {
            if (i == mData.size() - 1) {
                isFirst = false;
            }
            setTotalNum(holder.addView.getmNum());
        }
        return view;
    }

    @Override
    public void setNum(int position, int num) {
        if (mContext instanceof OrderDetailActivity) {
            if (num == 0) {
                mData.remove(position);
                if (mData.size() == 0) {
                    ((OrderDetailActivity) mContext).setTotalNum(0);
                }
            } else mData.get(position).num = num;
        } else if (mContext instanceof UsualActivity) {
            if (num > 0) {
                for (int i = 0; i < mPositions.size(); i++) {
                    if (mPositions.get(i).intValue() == position) {
                        mPositions.remove(i);
                    }
                }
                mData.get(position).num = num;
                mPositions.add(position);
            } else {
                mPositions.remove(position);
            }
        }
        setTotalNum0();
        notifyDataSetChanged();
    }

    public void setTotalNum0() {
        mTotalNum = 0;
        isFirst = true;
    }

    private void setTotalNum(int num) {
        mTotalNum += num;
        if (mContext instanceof OrderDetailActivity) {
            ((OrderDetailActivity) mContext).setTotalNum(mTotalNum);
        } else if (mContext instanceof UsualActivity) {
            ((UsualActivity) mContext).setTotalNum(mTotalNum);
        }
    }

    public List<Integer> getmPositions() {
        return mPositions;
    }

    public void setmPositions(List<Integer> mPositions) {
        this.mPositions = mPositions;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.add_view)
        AddView addView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
