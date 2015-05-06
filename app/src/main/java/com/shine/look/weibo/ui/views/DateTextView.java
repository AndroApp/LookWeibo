package com.shine.look.weibo.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:
 */
public class DateTextView extends TextView {


    public DateTextView(Context context) {
        super(context);
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTextByDate(String dateStr) {
        setText(dateFormat(dateStr));
    }

    public String dateFormat(String dateStr) {
        String dateFormat = null;
        //用于将英文格式的日期转换为date
        SimpleDateFormat usSDF = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
        //将date转换为中文格式的日期
        SimpleDateFormat chSDF;
        try {
            Date date = usSDF.parse(dateStr);
            //进行日期判断
            Calendar calendar = Calendar.getInstance();
            //当前时间戳
            long currentTime = calendar.getTimeInMillis();
            //时间差
            long diffTime = currentTime - date.getTime();
            //当前年份
            int currentYear = calendar.get(Calendar.YEAR);
            //date的年份
            calendar.setTimeInMillis(date.getTime());
            int dateYear = calendar.get(Calendar.YEAR);
            if (currentYear - dateYear > 0) {//大于1年
                //格式'yyyy年MM月dd日 HH:MM'
                StringBuffer sb = new StringBuffer();
                sb.append("yyyy").append(getContext().getString(R.string.year)).append("MM")
                        .append(getContext().getString(R.string.month)).append("dd")
                        .append(getContext().getString(R.string.day)).append("HH:MM");
                chSDF = new SimpleDateFormat(sb.toString(), Locale.CHINA);
                dateFormat = chSDF.format(date);
            } else if (diffTime > Constants.DAY_IN_MILLIS * 2) {//大于2天
                //格式'MM月dd日 HH:MM'
                StringBuffer sb = new StringBuffer();
                sb.append("MM").append(getContext().getString(R.string.month)).append("dd")
                        .append(getContext().getString(R.string.day)).append("HH:MM");
                chSDF = new SimpleDateFormat(sb.toString());
                dateFormat = chSDF.format(date);
            } else if (diffTime > Constants.DAY_IN_MILLIS) {//大于1天
                //格式'昨天 HH:MM'
                chSDF = new SimpleDateFormat(getContext().getString(R.string.Yesterday) + "HH:MM");
                dateFormat = chSDF.format(date);
            } else if (diffTime > Constants.HOUR_IN_MILLIS) {//大于1小时
                //格式 xx小时前
                int hour = (int) (diffTime / Constants.HOUR_IN_MILLIS);
                return hour + getContext().getString(R.string.hour_ago);
            } else if (diffTime > Constants.MINUTES_IN_MILLIS * 2) {//大于2分钟
                int minutes = (int) (diffTime / Constants.MINUTES_IN_MILLIS);
                return minutes + getContext().getString(R.string.minutes_ago);
            } else {
                setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                return getContext().getString(R.string.just);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
    }
}
