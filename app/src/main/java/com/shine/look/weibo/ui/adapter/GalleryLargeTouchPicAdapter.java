package com.shine.look.weibo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shine.look.weibo.bean.ThumbnailPic;
import com.shine.look.weibo.ui.fragment.GalleryLargeTouchPicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-05-16
 * Description:
 */
public class GalleryLargeTouchPicAdapter extends FragmentStatePagerAdapter {

    private List<ThumbnailPic> mData;
    private GalleryLargeTouchPicFragment.OnPicLoadingEnd mOnPicLoadingEnd;

    public GalleryLargeTouchPicAdapter(FragmentManager fm) {
        super(fm);
        mData = new ArrayList<>();
    }

    public void addItems(List<ThumbnailPic> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        GalleryLargeTouchPicFragment fragment = GalleryLargeTouchPicFragment.getInstance(mData.get(position));
        fragment.setOnPicLoadingEnd(mOnPicLoadingEnd);
        return fragment;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void setOnPicLoadingEnd(GalleryLargeTouchPicFragment.OnPicLoadingEnd onPicLoadingEnd) {
        mOnPicLoadingEnd = onPicLoadingEnd;
    }
}
