package com.shine.look.weibo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.ThumbnailPic;
import com.shine.look.weibo.ui.utils.LabelTransform;
import com.shine.look.weibo.utils.Constants;
import com.shine.look.weibo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class ThumbnailPicAdapter extends RecyclerView.Adapter<ThumbnailPicAdapter.ThumbnailViewHolder> {

    private final Context mContext;
    private final LabelTransform mTransformation;

    private List<ThumbnailPic> mData;
    private int mPicSize;


    public ThumbnailPicAdapter(Context context, List<ThumbnailPic> data) {
        this.mContext = context;
        this.mData = data;
        this.mPicSize = Utils.dpToPx(100);
        this.mTransformation = new LabelTransform();
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_thumbnail_pic, parent, false);
        return new ThumbnailViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        int urlSize = mData.size();
        if (urlSize > 1 && urlSize <= 9) {
            String picUrl = mData.get(position).thumbnail_pic.replace(Constants.URL_THUMBNAIL_PATH, Constants.URL_BMIDDLE_PATH);
            RequestCreator requestCreator = Picasso.with(mContext).load(picUrl);
            if (picUrl.indexOf(".gif") != -1) {
                requestCreator.transform(mTransformation);
            }
            requestCreator.resize(mPicSize, mPicSize)
                    .centerCrop()
                    .into(holder.ivThumbnailPic);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ThumbnailViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ivThumbnailPic)
        ImageView ivThumbnailPic;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
