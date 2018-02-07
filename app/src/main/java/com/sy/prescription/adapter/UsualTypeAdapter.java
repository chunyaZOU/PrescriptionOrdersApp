package com.sy.prescription.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sy.prescription.OrderDetailActivity;
import com.sy.prescription.R;
import com.sy.prescription.model.OrderInfo;
import com.sy.prescription.model.UsualType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ygs on 2018/2/3.
 */

public class UsualTypeAdapter extends BaseAdapter {


    private Context mContext;
    private List<UsualType> mData;
    private LayoutInflater mInflate;
    private int mSelectedPos;

    public UsualTypeAdapter(Context context, List<UsualType> usualTypes) {
        mContext = context;
        mData = usualTypes;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflate.inflate(R.layout.view_type_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvText.setTag(i + "");
        if (i == mSelectedPos) {
            holder.tvText.setSelected(true);
        } else {
            holder.tvText.setSelected(false);
        }
        if (holder.tvText.isSelected()) {
            holder.tvText.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.tvText.setTextColor(mContext.getResources().getColor(R.color.black_light));
        }
        final UsualType usualType = mData.get(i);
        holder.tvText.setText(usualType.typeName);
        holder.tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {

                } else {
                    mSelectedPos = Integer.parseInt(v.getTag().toString());
                    notifyDataSetChanged();
                }
            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.text)
        TextView tvText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
