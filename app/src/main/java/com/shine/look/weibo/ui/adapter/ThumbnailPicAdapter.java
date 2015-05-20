package com.shine.look.weibo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.ThumbnailPic;
import com.shine.look.weibo.ui.utils.LabelTransform;
import com.shine.look.weibo.utils.Constants;
import com.shine.look.weibo.utils.Utils;

import java.util.ArrayList;

/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class ThumbnailPicAdapter extends RecyclerView.Adapter<ThumbnailPicAdapter.ThumbnailViewHolder> implements View.OnClickListener {

    private final Context mContext;
    private final LabelTransform mTransformation;
    private final RequestManager mGlide;

    private ArrayList<ThumbnailPic> mData;
    private OnThumbnailPicListener mListener;
    private int mPicSize;


    public ThumbnailPicAdapter(Context context, ArrayList<ThumbnailPic> data, RequestManager glide) {
        this.mContext = context;
        this.mData = data;
        this.mPicSize = Utils.dpToPx(100);
        this.mTransformation = new LabelTransform(context);
        this.mGlide = glide;
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_thumbnail_pic, parent, false);
        ThumbnailViewHolder holder = new ThumbnailViewHolder(rootView);
        holder.ivThumbnailPic.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        int urlSize = mData.size();
        if (urlSize > 1 && urlSize <= 9) {
            holder.itemView.setTag(position);
            String picUrl = mData.get(position).thumbnail_pic.replace(Constants.URL_THUMBNAIL_PATH, Constants.URL_BMIDDLE_PATH);
            BitmapRequestBuilder bitmapRequestBuilder = mGlide.load(picUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL);
            bitmapRequestBuilder
                    .override(mPicSize, mPicSize)
                    .centerCrop();
            if (picUrl.indexOf(".gif") != -1) {
                bitmapRequestBuilder.transform(mTransformation);
            }
            bitmapRequestBuilder.into(holder.ivThumbnailPic);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivThumbnailPic:
                if (mListener != null) {
                    mListener.onThumbnailPicClick(v, (Integer) v.getTag(), mData);
                }
                break;
        }


    }

    public static class ThumbnailViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnailPic;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            ivThumbnailPic = (ImageView) itemView.findViewById(R.id.ivThumbnailPic);
        }
    }

    public void setOnThumbnailPicListener(OnThumbnailPicListener listener) {
        this.mListener = listener;
    }


    public interface OnThumbnailPicListener {
        public void onThumbnailPicClick(View view, int position, ArrayList<ThumbnailPic> data);
    }
}
