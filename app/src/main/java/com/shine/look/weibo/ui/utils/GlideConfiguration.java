package com.shine.look.weibo.ui.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.shine.look.weibo.R;

/**
 * User:Shine
 * Date:2015-05-15
 * Description:
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        ViewTarget.setTagId(R.id.pic_tag_id);

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
