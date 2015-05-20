package com.shine.look.weibo.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import com.shine.look.weibo.ui.activity.BaseActivity;
import com.shine.look.weibo.utils.Constants;

/**
 * User:Shine
 * Date:2015-05-20
 * Description:
 */
public class WeiboUrlSpan extends URLSpan {

    public static String START_LOCATION = "com.shine.look.weibo.startLocation";

    public WeiboUrlSpan(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        int[] startLocation = new int[2];
        widget.getLocationOnScreen(startLocation);

        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        intent.putExtra(START_LOCATION, startLocation[1]);
        intent.putExtra(Constants.ARG_TOOLBAR_IS_MENU, ((BaseActivity) widget.getContext()).toolBarIsMenu());
        intent.putExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, ((BaseActivity) widget.getContext()).toolBarMenuResId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}