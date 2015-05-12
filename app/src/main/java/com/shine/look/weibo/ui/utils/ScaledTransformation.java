package com.shine.look.weibo.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.shine.look.weibo.R;
import com.shine.look.weibo.WeiboApplication;
import com.shine.look.weibo.utils.Utils;
import com.squareup.picasso.Transformation;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class ScaledTransformation implements Transformation {

    private static final int MAX_HEIGHT = Utils.getScreenHeight();
    private final Paint mBgPaint;
    private final Paint mTextPaint;
    private final Paint mLabelPaint;
    private final Bitmap mGifLabelBitmap;
    private final Bitmap mLongLabelBitmap;

    private boolean isGif;
    private int mBitmapWidth;

    public ScaledTransformation() {
        Context context = WeiboApplication.getContext();
        mBgPaint = new Paint();
        mBgPaint.setColor(context.getResources().getColor(R.color.colorAccent));
        mBgPaint.setAntiAlias(true);
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(Utils.dpToPx(20));
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setAntiAlias(true);
        mLabelPaint = new Paint();
        mGifLabelBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_gif_label);
        mLongLabelBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_long_chart_label);
    }

    @Override
    public Bitmap transform(Bitmap source) {

        float scaledSize = (float) mBitmapWidth / source.getWidth();//得到需要放大的比例
        int scaledMaxHeight = (int) (MAX_HEIGHT / scaledSize);//原图的最大高度
        int sourceHeight = source.getHeight();//原图高度
        int sourceWidth = source.getWidth();//原图宽度
        if (sourceHeight > scaledMaxHeight) {//如果原图高度大于原图最大高度，则先进行裁剪在放大
            //裁剪
            Bitmap scaledMaxBitmap = Bitmap.createBitmap(source, 0, 0, sourceWidth, scaledMaxHeight / 2);
            if (scaledMaxBitmap != source) {
                source.recycle();
            }
            //放大
            Bitmap bitmap = Bitmap.createScaledBitmap(scaledMaxBitmap, mBitmapWidth, MAX_HEIGHT / 2, true);
            if (bitmap != scaledMaxBitmap) {
                scaledMaxBitmap.recycle();
            }
            //添加长图标签
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(mLongLabelBitmap, mBitmapWidth - mLongLabelBitmap.getWidth(), 0, mLabelPaint);
            return bitmap;
        } else {
            int scaledHeight = (int) (source.getHeight() * scaledSize);//要放大的高度
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, mBitmapWidth, scaledHeight, true);
            if (scaledBitmap != source) {
                source.recycle();
            }
            if (isGif) {//如果是GIF，则添加GIF标签
                Canvas canvas = new Canvas(scaledBitmap);
                canvas.drawBitmap(mGifLabelBitmap, mBitmapWidth - mGifLabelBitmap.getWidth(), 0, mLabelPaint);
            }
            return scaledBitmap;
        }
    }


    public void setBitmapWidth(int bitmapWidth) {
        this.mBitmapWidth = bitmapWidth;
    }

    public void setGifLabel(boolean label) {
        this.isGif = label;
    }

    @Override
    public String key() {
        return "scaled_transformation";
    }
}
