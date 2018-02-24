package com.sy.prescription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sy.prescription.OrderDetailActivity;
import com.sy.prescription.R;
import com.sy.prescription.model.OrderInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ygs on 2018/2/3.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    public static final int FLAG_ALL = 0;
    public static final int FLAG_Y = 1;
    public static final int FLAG_N = 2;

    private Context mContext;
    private List<OrderInfo> mData;
    private LayoutInflater mInflate;
    private int mSuccessFlag = FLAG_ALL;

    public OrderAdapter(Context context, List<OrderInfo> mOrderList) {
        mContext = context;
        mData = mOrderList;
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.view_order_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        final OrderInfo orderInfo = mData.get(position);
        holder.tvOrderNum.setText(orderInfo.cardNum);
        if (mSuccessFlag == 0) {
            holder.tvSuccess.setText(orderInfo.isSuccess ? "是" : "否");
        } else if (mSuccessFlag == 1) {
            holder.tvSuccess.setText("是");
        } else if (mSuccessFlag == 2) {
            holder.tvSuccess.setText("否");
        }
        holder.tvCommission.setText(orderInfo.commission + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailActivity.startAct(mContext, orderInfo.cardNum);
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

    public int getmSuccessFlag() {
        return mSuccessFlag;
    }

    public void setmSuccessFlag(int mSuccessFlag) {
        this.mSuccessFlag = mSuccessFlag;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order_num)
        TextView tvOrderNum;
        @BindView(R.id.tv_success)
        TextView tvSuccess;
        @BindView(R.id.tv_commission)
        TextView tvCommission;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
