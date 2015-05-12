package com.shine.look.weibo.ui.utils;

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

    private final Paint mBGPaint;
    private final Paint mTextPaint;
    private final Rect mRect;
    private final int mBaseLine;

    public LabelTransform() {
        mBGPaint = new Paint();
        mTextPaint = new Paint();
        mBGPaint.setColor(WeiboApplication.getContext().getResources().getColor(R.color.colorAccent));
        mTextPaint.setColor(WeiboApplication.getContext().getResources().getColor(R.color.white));
        mTextPaint.setTextSize(Utils.dpToPx(14));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
        int size = Utils.dpToPx(100);
        int left = size - Utils.dpToPx(35);
        int top = size - Utils.dpToPx(20);
        mRect = new Rect(left, top, size, size);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        mBaseLine = mRect.top + (mRect.bottom - mRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Canvas canvas = new Canvas(source);
        canvas.drawRect(mRect, mBGPaint);
        canvas.drawText(LABEL_TEXT_GIF, mRect.centerX(), mBaseLine, mTextPaint);
        return source;
    }

    @Override
    public String key() {
        return "LabelTransform";
    }
}
