package com.sy.prescription.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.prescription.R;
import com.sy.prescription.adapter.KeyBoardAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ygs on 2017/3/10.
 */

public class VirtualKeyboardView extends RelativeLayout {

    Context context;
    private ArrayList<Map<String, String>> valueList;
    private ViewHolder holder;

    public VirtualKeyboardView(Context context) {
        super(context, null);
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.num_input_view, null);
        holder = new ViewHolder(view);
        valueList = new ArrayList<>();
        setView();
        addView(view);
    }


    public ArrayList<Map<String, String>> getValueList() {
        return valueList;
    }

    public LinearLayout getLlBack() {
        return holder.llBack;
    }


    public TextView getTvPay() {
        return holder.tvPay;
    }


    public GridView getGridView() {
        return holder.gvNumPan;
    }

    private void setView() {

        /* 初始化按钮上应该显示的数字 */
        for (int i = 0; i < 9; i++) {
            Map<String, String> map = new HashMap<>();
            switch (i) {
                case 8:
                    map.put("name", String.valueOf(9));
                    break;
                case 7:
                    map.put("name", String.valueOf(8));
                    break;
                case 6:
                    map.put("name", String.valueOf(7));
                    break;
                case 5:
                    map.put("name", String.valueOf(6));
                    break;
                case 4:
                    map.put("name", String.valueOf(5));
                    break;
                case 3:
                    map.put("name", String.valueOf(4));
                    break;
                case 2:
                    map.put("name", String.valueOf(3));
                    break;
                case 1:
                    map.put("name", String.valueOf(2));
                    break;
                case 0:
                    map.put("name", String.valueOf(1));
                    break;
            }
            valueList.add(map);
        }
        for (int i = 0; i < 3; i++) {
            Map<String, String> map = new HashMap<>();
            if (i == 0) {
                map.put("name", "00");
            } else if (i == 1) {
                map.put("name", "0");
            } else if (i == 2) {
                map.put("name", ".");
            }
            valueList.add(map);
        }
        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(context, valueList);
        holder.gvNumPan.setAdapter(keyBoardAdapter);
    }

    static class ViewHolder {
        @BindView(R.id.gvNumPan)
        GridView gvNumPan;
        @BindView(R.id.llBack)
        LinearLayout llBack;
        @BindView(R.id.tvPay)
        TextView tvPay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}