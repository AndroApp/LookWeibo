package com.shine.look.weibo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

    private static int TOAST_Y_OFFSET = Utils.dpToPx(24);

    public static void show(String str) {
        Context context = WeiboApplication.getContext();
        show(context, str, 0);
    }

    public static void show(int resId) {
        Context context = WeiboApplication.getContext();
        show(context, context.getString(resId), 0);
    }


    private static void show(Context context, CharSequence text, int gravity) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        TextView messageText = (TextView) toastView.findViewById(R.id.toast_message);
        messageText.setText(text);
        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        if (gravity > 0)
            toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    public static void show(int textId, int resId) {
        Context context = WeiboApplication.getContext();
        show(context, context.getString(textId), resId, 0);
    }

    public static void show(String str, int resId) {
        Context context = WeiboApplication.getContext();
        show(context, str, resId, 0);
    }

    public static void show(int textId, int resId, int gravity) {
        Context context = WeiboApplication.getContext();
        show(context, context.getString(textId), resId, gravity);
    }

    public static void show(String str, int resId, int gravity) {
        Context context = WeiboApplication.getContext();
        show(context, str, resId, gravity);
    }

    private static void show(Context context, String str, int resId, int gravity) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast_image, null);
        TextView messageText = (TextView) toastView.findViewById(R.id.toast_message);
        ImageView image = (ImageView) toastView.findViewById(R.id.toast_image);
        messageText.setText(str);
        image.setImageResource(resId);
        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        if (gravity > 0)
            toast.setGravity(gravity, 0, TOAST_Y_OFFSET);
        toast.show();
    }
}
