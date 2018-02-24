package com.sy.prescription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MedicalAdapter extends RecyclerView.Adapter<MedicalAdapter.ViewHolder> implements AddView.ChangeListener {

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.view_medical_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MedicalInfo orderInfo = mData.get(position);
        holder.tvName.setText(orderInfo.name);
        holder.addView.setTag(position + "");
        holder.addView.setmListener(this);
        holder.addView.setmNum(orderInfo.num);
        if (isFirst) {
            if (position == mData.size() - 1) {
                isFirst = false;
            }
            setTotalNum(holder.addView.getmNum());
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mData.size();
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
            for (int i = 0; i < mPositions.size(); i++) {
                //此条已被选择，则先清除后再添加
                if (mPositions.get(i).intValue() == position) {
                    mPositions.remove(i);
                }
            }
            mData.get(position).num = num;
            //只有num>0才被添加
            if (num > 0) mPositions.add(position);
        }
        setTotalNum0();
    }

    public void setTotalNum0() {
        mTotalNum = 0;
        isFirst = true;
        notifyDataSetChanged();
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.add_view)
        AddView addView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
