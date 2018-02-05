package com.sy.prescription.view.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;


import com.sy.prescription.R;
import com.sy.prescription.util.ResourceUtil;
import com.sy.prescription.util.ScreenUtil;

import java.util.ArrayList;

public class ActionSheetPanel extends BasePanel {
    private ActionItemView mCancelView;
    private LinearLayout mContainerLayout;
    private ArrayList<ActionSheetItem> mItemList = new ArrayList<ActionSheetItem>();

    private OnActionSheetClickListener mListener;

    private int mGravity = Gravity.CENTER;

    public ActionSheetPanel(Context context) {
        super(context);
    }

    @Override
    protected View onCreateContentView() {
        ActionSheetScrollView scrollView = new ActionSheetScrollView(mContext);
        scrollView.setBackgroundColor(ResourceUtil.getColor(R.color.white));
        mContainerLayout = new LinearLayout(mContext);
        mContainerLayout.setOrientation(LinearLayout.VERTICAL);
        mCancelView = new ActionItemView(mContext);
        mCancelView.setTextColor(ResourceUtil.getColor(R.color.actionsheet_red));
        mCancelView.setText(ResourceUtil.getString(R.string.action_sheet_cancel));
        mCancelView.setEnableDivider(false);
        mCancelView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });
        scrollView.addView(mContainerLayout);
        return scrollView;
    }

    public void setEnableCancelButton(boolean enable) {
        mCancelView.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void setCancelText(String text) {
        mCancelView.setText(text);
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public void setSheetItemClickListener(OnActionSheetClickListener listener) {
        mListener = listener;
    }

    public void addSheetItem(ActionSheetItem item) {
        mItemList.add(item);
    }

    public void addSheetItems(ArrayList<ActionSheetItem> list) {
        mItemList.addAll(list);
    }

    public void clearSheetItem() {
        mItemList.clear();
    }

    @Override
    public void showPanel() {
        setupPanel();
        super.showPanel();
    }

    private void setupPanel() {
        mContainerLayout.removeAllViewsInLayout();
        LayoutParams lp;
        int size = mItemList.size();
        boolean enableCancel = mCancelView.getVisibility() == View.VISIBLE;
        for (int i = 0; i < size; i++) {
            final ActionSheetItem sheetItem = mItemList.get(i);
            ActionItemView textView = new ActionItemView(mContext);
            textView.setText(sheetItem.title);
            textView.setTextColor(ResourceUtil.getColor(sheetItem.colorResId));
            textView.setTextGravity(mGravity);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onActionSheetItemClick(sheetItem.id);
                    }
                    hidePanel();
                }
            });
            if (i == size - 1) {
                if (!enableCancel) {
                    textView.setEnableDivider(false);
                }
            }
            lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, ResourceUtil.getDimen(R.dimen.space_50));
            mContainerLayout.addView(textView, lp);
        }

        if (enableCancel) {
            lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, ResourceUtil.getDimen(R.dimen.space_50));
            mCancelView.setTextGravity(mGravity);
            mContainerLayout.addView(mCancelView, lp);
        }
    }

    private class ActionItemView extends FrameLayout {
        private TextView mTextView;
        private View mDivider;
        private LayoutParams mTextLp;

        public ActionItemView(Context context) {
            super(context);

            mTextView = new TextView(getContext());
            mTextView.setTextColor(ResourceUtil.getColor(R.color.actionsheet_black));
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceUtil.getDimen(R.dimen.space_16));
            mTextView.setSingleLine();
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.leftMargin = ResourceUtil.getDimen(R.dimen.space_16);
            lp.rightMargin = lp.leftMargin;
            this.addView(mTextView, lp);
            mTextLp = lp;

            mDivider = new View(getContext());
            mDivider.setBackgroundColor(ResourceUtil.getColor(R.color.app_theme_gray));
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResourceUtil.getDimen(R.dimen.divider_height));
            lp.gravity = Gravity.BOTTOM;
            this.addView(mDivider, lp);

            this.setBackgroundResource(R.drawable.default_listview_seletor);
        }

        public void setTextGravity(int gravity) {
            mTextLp.gravity = gravity;
        }

        public void setText(String text) {
            mTextView.setText(text);
        }

        public void setTextColor(int color) {
            mTextView.setTextColor(color);
        }

        public void setEnableDivider(boolean enable) {
            mDivider.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    private class ActionSheetScrollView extends ScrollView {

        public ActionSheetScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            int maxHeight = (int) (ScreenUtil.getScreenHeigh(mContext) * 0.8f);
            if (measuredHeight > maxHeight) {
                measuredHeight = maxHeight;
                setMeasuredDimension(measuredWidth, measuredHeight);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    public static class ActionSheetItem {
        public String id;
        public String title;
        public int colorResId;

        public ActionSheetItem() {
            colorResId = R.color.colorPrimary;
        }
    }


    public interface OnActionSheetClickListener {
        void onActionSheetItemClick(String id);
    }

}
