package com.shine.look.weibo.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.shine.look.weibo.R;
import com.shine.look.weibo.WeiboApplication;
import com.shine.look.weibo.utils.Utils;

/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class LabelTransform extends BitmapTransformation {

    private static final String LABEL_TEXT_GIF = "GIF";

    private final Paint mBGPaint;
    private final Paint mTextPaint;
    private final Rect mRect;
    private final int mBaseLine;

    public LabelTransform(Context context) {
        super(context);
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
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        Bitmap bitmap = Bitmap.createScaledBitmap(source, outWidth, outHeight, true);
        if (bitmap != source) {
            source.recycle();
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(mRect, mBGPaint);
        canvas.drawText(LABEL_TEXT_GIF, mRect.centerX(), mBaseLine, mTextPaint);
        return bitmap;
    }

    @Override
    public String getId() {
        return "com.shine.look.weibo.LabelTransform";
    }
}
