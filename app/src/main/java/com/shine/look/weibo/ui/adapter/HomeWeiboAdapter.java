package com.shine.look.weibo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.Status;
import com.shine.look.weibo.ui.views.ContextTextView;
import com.shine.look.weibo.ui.views.DateTextView;
import com.shine.look.weibo.ui.views.ThumbnailPicLayoutManager;
import com.shine.look.weibo.ui.views.ScaledTransformation;
import com.shine.look.weibo.utils.Constants;
import com.shine.look.weibo.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:
 */
public class HomeWeiboAdapter extends RecyclerView.Adapter<HomeWeiboAdapter.HomeWeiboViewHolder> {

    private static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private static final int COLUMN_COUNT = 3;

    private static final int mAvatarSize = 50;

    private final ScaledTransformation mTransformation;
    private final LayoutInflater mInflater;

    private List<Status> mData;
    private Context mContext;

    /**
     * 最后一个进行动画的item的位置
     */
    private int mLastAnimatedPosition = -1;

    private boolean isVisibleLoading;

    public HomeWeiboAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.mTransformation = new ScaledTransformation();
    }

    @Override
    public HomeWeiboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View loadingView = mInflater.inflate(R.layout.layout_recyclerview_footer, parent, false);
            return new HomeWeiboViewHolder(loadingView);
        } else {
            View rootView = mInflater.inflate(R.layout.item_home_weibo, parent, false);
            HomeWeiboViewHolder holder = new HomeWeiboViewHolder(rootView);
            holder.rvThumbnailPic.setLayoutManager(new ThumbnailPicLayoutManager(mContext, COLUMN_COUNT));
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(HomeWeiboViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        if (getItemViewType(position) != VIEW_TYPE_LOADING) {
            Status statusBean = mData.get(position);
            holder.tvUserName.setText(statusBean.user.screen_name);
            holder.tvCreatedTime.setTextByDate(statusBean.created_at);
            Spannable spannable = new SpannableString(Html.fromHtml(statusBean.source));
            spannable.setSpan(new ForegroundColorSpan(0) {
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                    ds.setColor(mContext.getResources().getColor(R.color.secondary_text));
                }
            }, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvSource.setText(spannable);
            holder.tvText.dealWithText(statusBean.text);
            Picasso.with(mContext)
                    .load(statusBean.user.profile_image_url)
                    .resize(mAvatarSize, mAvatarSize)
                    .centerCrop()
                    .into(holder.ivUserProfile);

            int picSize = statusBean.pic_urls.size();
            if (picSize == 0) {
                holder.ivLargePic.setVisibility(View.GONE);
                holder.rvThumbnailPic.setVisibility(View.GONE);
            } else if (picSize == 1) {
                holder.ivLargePic.setVisibility(View.VISIBLE);
                holder.rvThumbnailPic.setVisibility(View.GONE);
                String largeUrl = statusBean.pic_urls.get(0).thumbnail_pic.replace(Constants.URL_THUMBNAIL_PATH, Constants.URL_LARGE_PATH);
                Picasso.with(mContext)
                        .load(largeUrl)
                        .transform(mTransformation)
                        .into(holder.ivLargePic);
            } else if (picSize > 1) {
                holder.ivLargePic.setVisibility(View.GONE);
                holder.rvThumbnailPic.setVisibility(View.VISIBLE);
                holder.rvThumbnailPic.setAdapter(new ThumbnailPicAdapter(mContext, statusBean.pic_urls));
            }
        }
    }


    private void runEnterAnimation(View itemView, int position) {
        if (position > mLastAnimatedPosition) {
            mLastAnimatedPosition = position;
            itemView.setTranslationY(Utils.getScreenHeight());
            itemView.animate().translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(List<Status> dataList) {
        mData.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear(int lastVisibleItemPosition) {
        mData.clear();
        mLastAnimatedPosition = lastVisibleItemPosition;
    }

    public void addLoading() {
        isVisibleLoading = true;
        notifyItemInserted(mData.size());
    }

    public void removeLoading() {
        isVisibleLoading = false;
        notifyItemRemoved(mData.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isVisibleLoading) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    public static class HomeWeiboViewHolder extends RecyclerView.ViewHolder {
        @Optional
        @InjectView(R.id.ivUserProfile)
        ImageView ivUserProfile;
        @Optional
        @InjectView(R.id.tvUserName)
        TextView tvUserName;
        @Optional
        @InjectView(R.id.tvSource)
        TextView tvSource;
        @Optional
        @InjectView(R.id.tvCreatedTime)
        DateTextView tvCreatedTime;
        @Optional
        @InjectView(R.id.tvText)
        ContextTextView tvText;
        @Optional
        @InjectView(R.id.ivLargePic)
        ImageView ivLargePic;
        @Optional
        @InjectView(R.id.rvThumbnailPic)
        RecyclerView rvThumbnailPic;


        public HomeWeiboViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
