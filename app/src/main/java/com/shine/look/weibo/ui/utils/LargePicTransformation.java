package com.shine.look.weibo.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.shine.look.weibo.utils.Utils;

/**
 * User:Shine
 * Date:2015-05-15
 * Description:
 */
public class LargePicTransformation extends BitmapTransformation {

    private final int mScreenWidth;

    public LargePicTransformation(Context context) {
        super(context);
        mScreenWidth = Utils.getScreenWidth();
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Log.i("huangruimin", "bitmapWidth:" + toTransform.getWidth() + ",bitmapheight:" + toTransform.getHeight() + ",outWidth:" + outWidth + ",outheight:" + outHeight);
        float scaledSize = mScreenWidth / (float) toTransform.getWidth();
        int scaledHeight = (int) (scaledSize * toTransform.getHeight());
        if (outWidth == mScreenWidth && outHeight == scaledHeight) {
            return toTransform;
        }
        if (toTransform.getHeight() != scaledHeight && toTransform.getWidth() != mScreenWidth) {
            Bitmap bitmap = Bitmap.createScaledBitmap(toTransform, mScreenWidth, scaledHeight, true);
            if (bitmap != toTransform) {
                toTransform.recycle();
            }
            return bitmap;
        }
        return toTransform;
    }

    @Override
    public String getId() {
        return "com.shine.look.weibo.large";
    }
}
