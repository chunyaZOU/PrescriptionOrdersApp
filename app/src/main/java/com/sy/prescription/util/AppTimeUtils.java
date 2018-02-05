package com.sy.prescription.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.annotation.SuppressLint;
import android.content.Context;

import com.sy.prescription.R;

@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class AppTimeUtils {

    /***
     * String2Date date为yyyy-MM-dd HH:mm:ss
     ***/
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(dateString);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date parseDate(String dateString, String pFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pFormat);
            return format.parse(dateString);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /***
     * Date2String
     ***/
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /***
     * 设置日期想显示格式 dateString为yyyy-MM-dd HH:mm:ss
     **/
    public static String getInitDateFromFormatString(Context context, String dateString) {
        if (dateString != null) {
            // 时间精确到yyyy-MM-dd HH-mm-ss
            Date nowDate = new Date();
            Date date = parseDate(dateString);
            // 时间精确到yyyy-MM-dd
            Date nowDate_date = parseDate(formatDate(nowDate), "yyyy-MM-dd");
            Date date_day = parseDate(dateString, "yyyy-MM-dd");
            // 精确到年月日后的天数差
            long tempDate = (nowDate_date.getTime() - date_day.getTime()) / (1000 * 60 * 60 * 24);
            // 秒数差
            long tempTime = (nowDate.getTime() - date.getTime()) / 1000;
            if (tempDate == 0 && (tempTime < 60)) {
                return "刚刚";
            } else if (tempDate == 0 && ((tempTime / 60) < 60)) {
                return String.format("%d分钟前", tempTime / 60);
            } else if (tempDate == 0) {
                return String.format("%d小时前", tempTime / 3600);
            } else if (tempDate == 1) {
                return "昨天";
            } else if (tempDate == 2) {
                return "前天";
            } else if (tempDate < 7) {
                return String.format("%d天前", tempDate);
            } else {
                return formatDate(date, context.getString(R.string.custom_date_format));
            }
        }
        return dateString;
    }

    /***
     * 设置日期想显示格式 dateString为yyyy-MM-dd HH:mm:ss
     **/
    @SuppressWarnings("deprecation")
    public static String getInitDateTalkString(Context context, String dateString) {
        String str = "";
        if (dateString != null) {
            Date date = parseDate(dateString);
            // 时间精确到yyyy-MM-dd
            Date nowDate_date = parseDate(formatDate(new Date()), "yyyy-MM-dd");
            Date date_day = parseDate(dateString, "yyyy-MM-dd");
            // 精确到年月日后的天数差
            long tempDate = (nowDate_date.getTime() - date_day.getTime()) / (1000 * 60 * 60 * 24);
            if (tempDate == 0) {
                str = formatDate(date, context.getString(R.string.talk_today_format));
            } else if (tempDate == 1) {
                str = formatDate(date, context.getString(R.string.talk_yesterday_format));
            } else if (tempDate < 5) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                str = getWeekText(context, cal.get(Calendar.DAY_OF_WEEK)) + " "
                        + formatDate(date, context.getString(R.string.talk_today_format));
            } else if ((nowDate_date.getYear() - date_day.getYear()) == 0) {
                str = formatDate(date, context.getString(R.string.talk_date_month_format));
            } else {
                str = formatDate(date, context.getString(R.string.talk_date_format));
            }
            return str;
        }
        return dateString;
    }

    private static String getWeekText(Context context, int day) {
        int resId;
        switch (day) {
            case 2:
                resId = R.string.monday;
                break;
            case 3:
                resId = R.string.tuesday;
                break;
            case 4:
                resId = R.string.wednesday;
                break;
            case 5:
                resId = R.string.thursday;
                break;
            case 6:
                resId = R.string.friday;
                break;
            case 7:
                resId = R.string.saturday;
                break;
            default:
                resId = R.string.sunday;
        }
        return context.getResources().getString(resId);
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}