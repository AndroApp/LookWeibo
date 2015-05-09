package com.shine.look.weibo.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shine.look.weibo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:
 */
public class DateTextView extends TextView {

    private Calendar mCalendar;

    private int mCurrentYear;
    private int mCurrentDay;

    private String mYearFormatStr;
    private String mMonthFormatStr;


    public DateTextView(Context context) {
        super(context);
        init();
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mCalendar = Calendar.getInstance();
        //当前年份
        mCurrentYear = mCalendar.get(Calendar.YEAR);
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mYearFormatStr = "yyyy" + getResources().getString(R.string.year) + "MM" + getResources().getString(R.string.month) + "dd HH:mm";
        mMonthFormatStr = "MM" + getResources().getString(R.string.month) + "dd HH:mm";

    }

    public void setTextByDate(String dateStr) {
        setText(dateFormat(dateStr));
    }

    public String dateFormat(String dateStr) {
        String dateFormat;
        try {
            SimpleDateFormat sdf;
            Date date = new Date(dateStr);
            //获取是几年、几日、几小时、几分钟前发的微博
            mCalendar.setTimeInMillis(date.getTime());
            int dataYear = mCalendar.get(Calendar.YEAR);
            int dateDay = mCalendar.get(Calendar.DAY_OF_MONTH);
            int diffYear = mCurrentYear - dataYear;
            int diffDay = mCurrentDay - dateDay;
            long diffTime = Long.valueOf((System.currentTimeMillis() - date.getTime()) / 1000);
            long minuteTime = diffTime / 60;
            long hourTime = minuteTime / 60;
            if (diffTime < 0 || diffYear >= 1) {//大于等于1年
                sdf = new SimpleDateFormat(mYearFormatStr);
                dateFormat = sdf.format(date);
            } else if (diffDay >= 2) {//大于等于2天
                sdf = new SimpleDateFormat(mMonthFormatStr);
                dateFormat = sdf.format(date);
            } else if (diffDay == 1) {//昨天
                sdf = new SimpleDateFormat("HH:mm");
                dateFormat = getResources().getString(R.string.yesterday) + " " + sdf.format(date);
            } else if (minuteTime >= 60) {//今天几小时前
                dateFormat = hourTime + getResources().getString(R.string.hour_ago);
            } else if (minuteTime >= 3) {//几分钟前
                dateFormat = minuteTime + getResources().getString(R.string.minutes_ago);
            } else {
                dateFormat = getResources().getString(R.string.just);
            }
        } catch (Exception e) {
            dateFormat = getResources().getString(R.string.unknown);
        }

        return dateFormat;
    }
}
