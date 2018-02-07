package com.sy.prescription.adapter;

import android.content.Context;
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

public class OrderAdapter extends BaseAdapter {

    public static final int FLAG_ALL=0;
    public static final int FLAG_Y=1;
    public static final int FLAG_N=2;

    private Context mContext;
    private List<OrderInfo> mData;
    private LayoutInflater mInflate;
    private int mSuccessFlag=FLAG_ALL;

    public OrderAdapter(Context context, List<OrderInfo> mOrderList) {
        mContext = context;
        mData = mOrderList;
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
            view = mInflate.inflate(R.layout.view_order_layout, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final OrderInfo orderInfo = mData.get(i);
        holder.tvOrderNum.setText(orderInfo.cardNum);
        if (mSuccessFlag == 0) {
            holder.tvSuccess.setText(orderInfo.isSuccess ? "是" : "否");
        } else if (mSuccessFlag == 1) {
            holder.tvSuccess.setText("是");
        } else if (mSuccessFlag == 2) {
            holder.tvSuccess.setText("否");
        }
        holder.tvCommission.setText(orderInfo.commission + "");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailActivity.startAct(mContext,orderInfo.cardNum);
            }
        });
        return view;
    }

    public int getmSuccessFlag() {
        return mSuccessFlag;
    }

    public void setmSuccessFlag(int mSuccessFlag) {
        this.mSuccessFlag = mSuccessFlag;
    }

    static class ViewHolder {
        @BindView(R.id.tv_order_num)
        TextView tvOrderNum;
        @BindView(R.id.tv_success)
        TextView tvSuccess;
        @BindView(R.id.tv_commission)
        TextView tvCommission;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
