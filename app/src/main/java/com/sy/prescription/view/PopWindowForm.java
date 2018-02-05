package com.sy.prescription.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sy.prescription.R;
import com.sy.prescription.util.DensityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zcy on 2017/3/14.
 */

public class PopWindowForm {

    private String[] payWays = { "全部","微信支付", "支付宝"};
    private String[] payState = {"全部","收款成功", "未支付", "已撤销", "转入退款", "已关闭"};
    private String[] other = {"全部","退款", "卡券"};
    private Context context;
    private TextView tvNoPay;
    private FormView view;
    private String payWayTags = "", payStateTags = "";

    private PopupWindow initWindow(Context context) {
        final PopupWindow window = new PopupWindow(DensityUtils.getDisplayMetrics(context).widthPixels, LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setAnimationStyle(R.style.PopupAnimation);
        window.update();
        return window;
    }

    ViewHolder holder;

    public PopupWindow initNumInputWindow(final Context context, TextView tv, FormView view) {
        this.context = context;
        this.tvNoPay = tv;
        this.view = view;
        final PopupWindow window = initWindow(context);
        window.setBackgroundDrawable(new BitmapDrawable());
        View contentView = LayoutInflater.from(context).inflate(R.layout.form_pop_fixed_view, null);
        holder = new ViewHolder(contentView);
        holder.tvWechat.setOnClickListener(clickListener);
        holder.tvAlipay.setOnClickListener(clickListener);
        holder.tvWayAll.setOnClickListener(clickListener);
        holder.tvPaySuccess.setOnClickListener(clickListener);
        holder.tvPayNo.setOnClickListener(clickListener);
        holder.tvBackout.setOnClickListener(clickListener);
        holder.tvToBack.setOnClickListener(clickListener);
        holder.tvClose.setOnClickListener(clickListener);
        holder.tvStatusAll.setOnClickListener(clickListener);
        holder.tvReset.setOnClickListener(clickListener);
        holder.tvConfirm.setOnClickListener(clickListener);
        window.setContentView(contentView);
        return window;
    }

    private void initTagViews(ViewGroup layout, String[] strs, final String tag) {
        MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        for (int i = 0; i < strs.length; i++) {
            TextView tv = new TextView(context);
            tv.setTextSize(14);
            if (tag.equals("way")) {
                tv.setId(i);
            } else if (tag.equals("state")) {
                tv.setId(i + 3);
            }
            tv.setTag(tag);


            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine();
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setTextColor(context.getResources().getColor(R.color.text_gray));
            tv.setBackground(context.getResources().getDrawable(R.drawable.hot_tag_border_bg));
            try {
                tv.setText(strs[i]);
                final int item = i;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setBackgroundResource(R.drawable.count_title_bg);
                        ((TextView) view).setTextColor(context.getResources().getColor(R.color.blue));
                        tvNoPay.setText("已筛选");
                        String selection = ((TextView) view).getText().toString();
                        switch (selection) {
                            case "微信支付":
                                payWayTags += "1,";
                                break;
                            case "支付宝":
                                payWayTags += "2,";
                                break;
                            /*case "全部":
                                payWayTags += "3,";
                                break;*/
                            case "支付成功":
                                payStateTags += "2,";
                                break;
                            case "未支付":
                                payStateTags += "1,";
                                break;
                            case "已撤销":
                                payStateTags += "9,";
                                break;
                            case "转入退款":
                                payStateTags += "11,";
                                break;
                            case "已关闭":
                                payStateTags += "3,";
                                break;
                            case "全部":
                                payStateTags += "0,";
                                break;
                        }
                        String viewTag = (String) view.getTag();
                        if (viewTag.equals("way")) {
                            payWayTags += ((TextView) view).getText().toString() + ",";
                        } else if (viewTag.equals("state")) {
                            payStateTags += ((TextView) view).getText().toString() + ",";
                        }
                        //setTag(((TextView) view).getText().toString() + "," + view.getTag());
                    }
                });
            } catch (Exception e) {
            }
            layout.addView(tv);
        }
    }

    private boolean isWechat, isAlipay, isWayAll, isPaySuccess, isPayNo, isBackout, isToBack, isClose, isStatusAll;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvReset:
                    //tvNoPay.setText("筛选");
                    isWechat = isAlipay = isWayAll = isPaySuccess = isPayNo = isBackout = isToBack = isClose = isStatusAll = false;
                    payWayTags = "";
                    payStateTags = "";
                    view.hidePop();
                    break;
                case R.id.tvConfirm:
                    view.hidePop();
                    if (isWechat) payWayTags += "1,";
                    if (isAlipay) payWayTags += "2,";
                    if (isPaySuccess) payStateTags += "2,";
                    if (isPayNo) payStateTags += "1,";
                    if (isBackout) payStateTags += "9,";
                    if (isToBack) payStateTags += "11,";
                    if (isClose) payStateTags += "3,";
                    view.selectConfirm(payWayTags, payStateTags);
                    break;
                case R.id.tvWechat:
                    if (isWechat) {
                        isWechat = false;
                        setUnselected(v);
                    } else {
                        isWechat = true;
                        setSelected(v);
                    }
                    break;
                case R.id.tvAlipay:
                    if (isAlipay) {
                        isAlipay = false;
                        setUnselected(v);
                    } else {
                        isAlipay = true;
                        setSelected(v);
                    }
                    break;
                case R.id.tvWayAll:
                    /*if (isWayAll) {
                        isWayAll = false;
                        setUnselected(v);
                    } else {*/
                    isWayAll = true;
                    payWayTags = "";
                   /* isWechat = false;
                    isAlipay = false;*/
                    if (isWechat) {
                        onClick(holder.tvWechat);
                    }
                    if (isAlipay) {
                        onClick(holder.tvAlipay);
                    }
                        /*setSelected(v);
                    }*/
                    break;
                case R.id.tvPaySuccess:
                    if (isPaySuccess) {
                        isPaySuccess = false;
                        setUnselected(v);
                    } else {
                        isPaySuccess = true;
                        setSelected(v);
                    }
                    break;
                case R.id.tvPayNo:
                    if (isPayNo) {
                        isPayNo = false;
                        setUnselected(v);
                    } else {
                        isPayNo = true;
                        setSelected(v);
                    }
                    break;
                case R.id.tvBackout:
                    if (isBackout) {
                        isBackout = false;
                        setUnselected(v);
                    } else {
                        isBackout = true;
                        setSelected(v);
                    }
                    break;
                case R.id.tvToBack:
                    if (isToBack) {
                        isToBack = false;
                        setUnselected(v);
                    } else {
                        isToBack = true;
                        setSelected(v);
                    }

                    break;
                case R.id.tvClose:
                    if (isClose) {
                        isClose = false;
                        setUnselected(v);
                    } else {
                        isClose = true;
                        setSelected(v);
                    }
                    break;
                case R.id.tvStatusAll:
                    /*if (isStatusAll) {
                        isStatusAll = false;
                        setUnselected(v);
                    } else {*/
                    isStatusAll = true;
                    payStateTags = "";
                    /*isPaySuccess = false;
                    isPayNo = false;
                    isBackout = false;
                    isToBack = false;
                    isClose = false;*/
                    if (isPaySuccess) {
                        onClick(holder.tvPaySuccess);
                    }
                    if (isPayNo) {
                        onClick(holder.tvPayNo);
                    }
                    if (isBackout) {
                        onClick(holder.tvBackout);
                    }
                    if (isToBack) {
                        onClick(holder.tvToBack);
                    }
                    if (isClose) {
                        onClick(holder.tvClose);
                    }
                        /*setSelected(v);
                    }*/
                    break;
            }
        }
    };

    private void setSelected(View v) {
        v.setBackgroundResource(R.drawable.count_title_bg);
        ((TextView) v).setTextColor(context.getResources().getColor(R.color.blue));
    }

    private void setUnselected(View v) {
        v.setBackgroundResource(R.drawable.hot_tag_border_bg);
        ((TextView) v).setTextColor(context.getResources().getColor(R.color.text_light));
    }

    static class ViewHolder {
        @BindView(R.id.tvWechat)
        TextView tvWechat;
        @BindView(R.id.tvAlipay)
        TextView tvAlipay;
        @BindView(R.id.tvWayAll)
        TextView tvWayAll;
        @BindView(R.id.tvPaySuccess)
        TextView tvPaySuccess;
        @BindView(R.id.tvPayNo)
        TextView tvPayNo;
        @BindView(R.id.tvBackout)
        TextView tvBackout;
        @BindView(R.id.tvToBack)
        TextView tvToBack;
        @BindView(R.id.tvClose)
        TextView tvClose;
        @BindView(R.id.tvStatusAll)
        TextView tvStatusAll;
        @BindView(R.id.tvReset)
        TextView tvReset;
        @BindView(R.id.tvConfirm)
        TextView tvConfirm;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
