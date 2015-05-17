package com.shine.look.weibo.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * User:Shine
 * Date:2015-05-13
 * Description:
 */
public class HorizontalProgressBar extends ProgressBar {
    public HorizontalProgressBar(Context context) {
        super(context);
        init();
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }


}
