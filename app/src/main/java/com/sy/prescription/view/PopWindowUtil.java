package com.sy.prescription.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.sy.prescription.R;
import com.sy.prescription.config.IMApplication;
import com.sy.prescription.util.DensityUtils;
import com.sy.prescription.util.ToastUtil;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zcy on 2017/3/10.
 */

public class PopWindowUtil {


    public static final int FLAG_UNENABLE = 0;//00&.不可用
    public static final int FLAG_ENABLE = 1;//00&.可用

    public static final int FLAG_ORSER_QUERY = 2;//00&.不可用，解除最长六位输入限制


    private int flag;
    private boolean hasPoint;
    private int pointNum;
    private String tempStr;
    private int selection;
    private String pwd;
    public EditText et;
    private ArrayList<Map<String, String>> valueList;
    private RefundView view;

    private PopupWindow initWindow(Context context) {
        final PopupWindow window = new PopupWindow(DensityUtils.getDisplayMetrics(context).widthPixels, DensityUtils.dp2Px(context, 220));
        window.setFocusable(true);
        window.setAnimationStyle(R.style.PopupAnimation);
        window.update();
        return window;
    }

    public PopupWindow initNumInputWindow(final Context context, EditText etView, int flag, RefundView view, int from) {
        this.view = view;
        this.flag = flag;
        this.et = etView;
        if (flag == FLAG_ENABLE) {
            //计算刚显示窗口时传入的EditText值得光标位置
            tempStr = et.getText().toString().trim();
            String strTemp1 = tempStr.substring(0, tempStr.indexOf("."));
            String strTemp2 = tempStr.substring(tempStr.indexOf(".") + 1);
            if (Integer.parseInt(strTemp1) > 0) {
                if (Integer.parseInt(strTemp2) > 0) {
                    //如果小数点位后的值大于0则光标在最后
                    selection = tempStr.length();
                    hasPoint = true;
                    pointNum = 2;
                } else {
                    ////如果小数点位后的值不大于0则光标在小数点前边
                    selection = tempStr.indexOf(".");
                }
                //et.setSelection(selection);//设置光表位置
            } else {
                if (Integer.parseInt(strTemp2) > 0) {
                    //如果小数点位后的值大于0则光标在最后
                    selection = tempStr.length();
                    hasPoint = true;
                    pointNum = 2;
                }
            }
            et.setSelection(selection);//设置光表位置
            /*if (from == ReceiveMoneyActivity.CASH_REQUEST) {
                et.addTextChangedListener(tw);
            }*/
        } else {
            pwd = et.getText().toString().trim();
            et.setSelection(pwd.length());
        }
        final PopupWindow window = initWindow(context);
        window.setBackgroundDrawable(new BitmapDrawable());
        View contentView = LayoutInflater.from(context).inflate(R.layout.keyboard_view, null);
        ViewHolder holder = new ViewHolder(contentView);
        window.setContentView(contentView);
        initKeyboard(holder, flag);
        return window;
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            view.cashCalculate();
        }
    };

    private void initKeyboard(ViewHolder holder, int flag) {
        holder.keyboard.getGridView().setOnItemClickListener(itemClickListener);
        holder.keyboard.getLlBack().setOnClickListener(clickListerer);
        holder.keyboard.getTvPay().setOnClickListener(clickListerer);
        valueList = holder.keyboard.getValueList();
        if (flag == FLAG_UNENABLE||flag == FLAG_ORSER_QUERY) {
            holder.keyboard.getTvPay().setBackgroundColor(IMApplication.getAppContext().getResources().getColor(R.color.blue));
            holder.keyboard.getTvPay().setText("确认");
        }
    }

    private View.OnClickListener clickListerer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llBack:
                    if (FLAG_UNENABLE == flag||FLAG_ORSER_QUERY == flag) {
                        if (!TextUtils.isEmpty(pwd)) {
                            int len = pwd.length();
                            if (len > 0) {
                                pwd = pwd.substring(0, len - 1);
                                et.setText(pwd);
                                et.setSelection(et.getText().toString().trim().length());
                            }
                        }
                    }else {
                        et.setText("0.00");
                        hasPoint = false;
                        pointNum = 0;
                        tempStr = "";
                        selection = 0;
                    }
                    break;
                case R.id.tvPay:
                    if (PopWindowUtil.FLAG_UNENABLE == flag) {
                        view.confirm();
                    } else {
                        view.pay();
                    }
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (flag == FLAG_UNENABLE) {
                if (i == 9 || i == 11) {
                    ToastUtil.show("此按钮不可用");
                    return;
                }
                if (!TextUtils.isEmpty(pwd) && pwd.length() > 5) {
                    return;
                }
                pwd = et.getText().toString().trim();
                pwd += valueList.get(i).get("name");
                et.setText(pwd);
                et.setSelection(et.getText().toString().trim().length());
            } else if (flag == FLAG_ENABLE) {
                String amount = et.getText().toString().trim();
                if (i == 11 && !hasPoint) {
                    //如果点击的是小数点时处理
                    if (amount.equals("0.00") && selection == 0) {
                        //值为0.00时，直接点“.”光标后移两位
                        selection++;
                        selection++;
                    } else {
                        //值不为0.00时，点“.”光标后移到小数点后
                        selection++;
                    }
                    hasPoint = true;
                    tempStr = amount;
                    et.setText(amount);
                    et.setSelection(selection);
                }
                if (i <= 10) {    //点击0~9按钮
                    if (!hasPoint) {
                        //未点小数点时处理
                        if (amount.equals("0.00")) {
                            //先点击00不做处理
                            if (i == 9) return;
                            if (i == 10) {
                                //值为0.00时，直接点“0”光标后移两位
                                hasPoint = true;
                                selection++;
                                selection++;
                            } else {
                                //先点击1-9数字，则点击数字拼上小数点及以后数字，光标后移一位
                                amount = valueList.get(i).get("name") + amount.substring(amount.indexOf("."));
                                selection++;
                            }
                            tempStr = amount;
                        } else {
                            //截取到光标位置的值拼接点击数字拼接光标后的值
                            amount = tempStr.substring(0, selection) + valueList.get(i).get("name")
                                    + tempStr.substring(selection);
                            if (i == 9) {
                                //如果点击00，光标后移两位
                                selection++;
                                selection++;
                            } else {
                                //如果不是双0，光标后移一位
                                selection++;
                            }
                            tempStr = amount;
                        }
                        et.setText(amount);
                        et.setSelection(selection);
                    } else {
                        //点击小数点后处理
                        if (i == 9) {
                            //如果点击00，不处理
                            return;
                        }
                        //小数点后位数
                        pointNum++;
                        if (pointNum == 1) {
                            //小数点后一位
                            amount = tempStr.substring(0, selection) + valueList.get(i).get("name") + "0";
                            selection++;
                        } else if (pointNum == 2) {
                            //小数点后两位
                            amount = tempStr.substring(0, selection) + valueList.get(i).get("name");
                            selection++;
                        } else {
                            //超出两位不处理
                            amount = tempStr;
                        }
                        tempStr = amount;
                        et.setText(amount);
                        et.setSelection(selection);
                    }
                }
            } else {
                if (i == 9 || i == 11) {
                    ToastUtil.show("此按钮不可用");
                    return;
                }
                pwd = et.getText().toString().trim();
                pwd += valueList.get(i).get("name");
                et.setText(pwd);
                et.setSelection(et.getText().toString().trim().length());
            }
        }
    };

    static class ViewHolder {
        @BindView(R.id.keyboard)
        com.sy.prescription.view.VirtualKeyboardView keyboard;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}