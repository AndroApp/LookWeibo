package com.shine.look.weibo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shine.look.weibo.R;
import com.shine.look.weibo.WeiboApplication;


/**
 * User: huangruimin
 * Date: 2015-04-20
 * Time: 14:19
 * Description:自定义Toast样式类
 */
public class ToastHelper {

    public static void show(String str) {
        Context context = WeiboApplication.getContext();
        show(context, str, 0);
    }

    public static void show(int resId) {
        Context context = WeiboApplication.getContext();
        show(context, context.getString(resId), 0);
    }

    public static void show(int resId, int gravity) {
        Context context = WeiboApplication.getContext();
        show(context, context.getString(resId), gravity);
    }

    public static void show(String message, int gravity) {
        Context context = WeiboApplication.getContext();
        show(context, message, gravity);
    }

    private static void show(Context context, String str, int gravity) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        TextView messageText = (TextView) toastView.findViewById(R.id.toast_message);
        messageText.setText(str);

        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        if (gravity > 0)
            toast.setGravity(gravity, 0, 0);
        toast.show();
    }
}
