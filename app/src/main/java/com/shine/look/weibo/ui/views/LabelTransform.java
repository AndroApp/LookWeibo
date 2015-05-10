package com.shine.look.weibo.ui.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shine.look.weibo.R;
import com.shine.look.weibo.WeiboApplication;
import com.shine.look.weibo.utils.Utils;
import com.squareup.picasso.Transformation;

/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class LabelTransform implements Transformation {

    private static final String LABEL_TEXT_GIF = "GIF";

    private final Paint mBGPain;
    private final Paint mTextPain;
    private final Rect mRect;
    private final int mBaseLine;

    public LabelTransform() {
        mBGPain = new Paint();
        mTextPain = new Paint();
        mBGPain.setColor(WeiboApplication.getContext().getResources().getColor(R.color.colorAccent));
        mTextPain.setColor(WeiboApplication.getContext().getResources().getColor(R.color.white));
        mTextPain.setTextSize(Utils.dpToPx(14));
        mTextPain.setTextAlign(Paint.Align.CENTER);
        int size = Utils.dpToPx(100);
        int left = size - Utils.dpToPx(35);
        int top = size - Utils.dpToPx(20);
        mRect = new Rect(left, top, size, size);
        Paint.FontMetricsInt fontMetrics = mTextPain.getFontMetricsInt();
        mBaseLine = mRect.top + (mRect.bottom - mRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Canvas canvas = new Canvas(source);
        canvas.drawRect(mRect, mBGPain);
        canvas.drawText(LABEL_TEXT_GIF, mRect.centerX(), mBaseLine, mTextPain);
        return source;
    }

    @Override
    public String key() {
        return "LabelTransform";
    }
}
