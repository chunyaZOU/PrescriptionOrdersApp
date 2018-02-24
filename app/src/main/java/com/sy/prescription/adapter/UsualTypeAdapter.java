package com.sy.prescription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sy.prescription.R;
import com.sy.prescription.model.UsualType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ygs on 2018/2/3.
 */

public class UsualTypeAdapter extends RecyclerView.Adapter<UsualTypeAdapter.ViewHolder> {


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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.view_type_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvText.setTag(position + "");
        if (position == mSelectedPos) {
            holder.tvText.setSelected(true);
        } else {
            holder.tvText.setSelected(false);
        }
        if (holder.tvText.isSelected()) {
            holder.tvText.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.tvText.setTextColor(mContext.getResources().getColor(R.color.black_light));
        }
        final UsualType usualType = mData.get(position);
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
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView tvText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
