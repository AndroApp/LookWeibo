package com.shine.look.weibo.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.Status;
import com.shine.look.weibo.ui.views.WeiboContentView;
import com.shine.look.weibo.utils.Constants;
import com.shine.look.weibo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:
 */
public class HomeWeiboAdapter extends RecyclerView.Adapter<HomeWeiboAdapter.HomeWeiboViewHolder> implements View.OnClickListener {

    private final LayoutInflater mInflater;
    private final RequestManager mRequestManager;
    private final WeiboContentView.OnPictureListener mOnPictureListener;

    private List<Status> mData;
    private boolean mAnimateItems = true;
    private OnFeedItemClickListener mOnFeedItemClickListener;

    /**
     * 最后一个进行动画的item的位置
     */
    private int mLastAnimatedPosition = -1;

    private boolean isVisibleLoading;
    private ThumbnailPicAdapter.OnThumbnailPicListener mOnThumbnailPicListener;


    public HomeWeiboAdapter(Activity activity, RequestManager requestManager, WeiboContentView.OnPictureListener listener) {
        this.mData = new ArrayList<>();
        this.mInflater = LayoutInflater.from(activity);
        this.mRequestManager = requestManager;
        this.mOnPictureListener = listener;
    }

    @Override
    public HomeWeiboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_LOADING) {
            View loadingView = mInflater.inflate(R.layout.layout_recyclerview_footer, parent, false);
            return new HomeWeiboViewHolder(loadingView);
        } else {
            View rootView = mInflater.inflate(R.layout.item_home_weibo, parent, false);
            HomeWeiboViewHolder holder = new HomeWeiboViewHolder(rootView);
            holder.flWeiboContent.setGlide(mRequestManager);
            holder.flWeiboContent.setOnPictureListener(mOnPictureListener);
            holder.flRetweetedContent.setGlide(mRequestManager);
            holder.flRetweetedContent.setOnPictureListener(mOnPictureListener);
            holder.flWeiboContent.setOnThumbnailPicListener(mOnThumbnailPicListener);
            holder.flRetweetedContent.setOnThumbnailPicListener(mOnThumbnailPicListener);
            holder.btnReposts.setOnClickListener(this);
            holder.btnComments.setOnClickListener(this);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(HomeWeiboViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        if (getItemViewType(position) != Constants.VIEW_TYPE_LOADING) {
            holder.btnReposts.setTag(position);
            holder.btnComments.setTag(position);
            Status status = mData.get(position);
            //微博内容
            holder.flWeiboContent.setWeiboContent(status, false);
            //转发的微博
            if (status.retweeted_status != null) {
                holder.cvRetweetedContent.setVisibility(View.VISIBLE);
                int[] i = new int[2];
                holder.flRetweetedContent.getLocationOnScreen(i);
                holder.flRetweetedContent.setWeiboContent(status.retweeted_status, true);
            } else {
                holder.cvRetweetedContent.setVisibility(View.GONE);
            }

            //转发数，评论数，赞数
            if (status.reposts_count > 0) {
                holder.tvRepostsCount.setText("" + status.reposts_count);
                holder.tvRepostsCount.setVisibility(View.VISIBLE);
            } else {
                holder.tvRepostsCount.setVisibility(View.GONE);
            }
            if (status.comments_count > 0) {
                holder.tvCommentsCount.setText("" + status.comments_count);
                holder.tvCommentsCount.setVisibility(View.VISIBLE);
            } else {
                holder.tvCommentsCount.setVisibility(View.GONE);
            }
            if (status.attitudes_count > 0) {
                holder.tvAttitudesCount.setText("" + status.attitudes_count);
                holder.tvAttitudesCount.setVisibility(View.VISIBLE);
            } else {
                holder.tvAttitudesCount.setVisibility(View.GONE);
            }

        }
    }


    private void runEnterAnimation(View itemView, int position) {
        if (mAnimateItems && position > mLastAnimatedPosition) {
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

    public void clear() {
        mData.clear();
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
            return Constants.VIEW_TYPE_LOADING;
        } else {
            return Constants.VIEW_TYPE_DEFAULT;
        }
    }

    public void closeAnimateItems() {
        if (this.mAnimateItems) {
            this.mAnimateItems = false;
        }
    }

    public long getMaxId() {
        return Long.parseLong(mData.get(mData.size() - 1).id);
    }

    public void setOnThumbnailPicListener(ThumbnailPicAdapter.OnThumbnailPicListener listener) {
        mOnThumbnailPicListener = listener;
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener listener) {
        this.mOnFeedItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReposts:
                if (mOnFeedItemClickListener != null) {
                    mOnFeedItemClickListener.onRepostsClick(v, (Integer) v.getTag());
                }
                break;
            case R.id.btnComments:
                if (mOnFeedItemClickListener != null) {
                    int i = (int) v.getTag();
                    mOnFeedItemClickListener.onCommentsClick(v, mData.get((Integer) v.getTag()).idstr);
                }
                break;
        }
    }

    public static class HomeWeiboViewHolder extends RecyclerView.ViewHolder {
        WeiboContentView flWeiboContent;
        WeiboContentView flRetweetedContent;
        CardView cvRetweetedContent;
        TextView tvAttitudesCount;
        TextView tvRepostsCount;
        TextView tvCommentsCount;
        ImageButton btnReposts;
        ImageButton btnComments;

        public HomeWeiboViewHolder(View itemView) {
            super(itemView);
            flWeiboContent = (WeiboContentView) itemView.findViewById(R.id.flWeiboContent);
            cvRetweetedContent = (CardView) itemView.findViewById(R.id.cvRetweetedContent);
            flRetweetedContent = (WeiboContentView) itemView.findViewById(R.id.flRetweetedContent);
            tvAttitudesCount = (TextView) itemView.findViewById(R.id.tvAttitudesCount);
            tvRepostsCount = (TextView) itemView.findViewById(R.id.tvRepostsCount);
            tvCommentsCount = (TextView) itemView.findViewById(R.id.tvCommentsCount);
            btnReposts = (ImageButton) itemView.findViewById(R.id.btnReposts);
            btnComments = (ImageButton) itemView.findViewById(R.id.btnComments);
        }
    }


    public interface OnFeedItemClickListener {
        public void onRepostsClick(View view, int position);

        public void onCommentsClick(View view, String weiboId);
    }
}
