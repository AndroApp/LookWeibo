package com.shine.look.weibo.ui.views;

import android.graphics.Bitmap;

import com.shine.look.weibo.utils.Utils;
import com.squareup.picasso.Transformation;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class ScaledTransformation implements Transformation {

    private final int mMaxHeight;
    private final int mScreenWidth;

    public ScaledTransformation() {
        mMaxHeight = Utils.dpToPx(400);
        mScreenWidth = Utils.getScreenWidth();
    }

    @Override
    public Bitmap transform(Bitmap source) {
        //缩放图片
        float scaledSize = (float) mScreenWidth / source.getWidth();
        int scaledHeight = (int) (source.getHeight() * scaledSize);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, mScreenWidth, scaledHeight, true);
        source.recycle();
        if (scaledBitmap.getHeight() < mMaxHeight) {
            return scaledBitmap;
        } else {
            //若图片高度大于最大限制高度则进行裁剪
            Bitmap bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, mScreenWidth, mMaxHeight);
            scaledBitmap.recycle();
            return bitmap;
        }
    }

    @Override
    public String key() {
        return "scaled_transformation";
    }
}
