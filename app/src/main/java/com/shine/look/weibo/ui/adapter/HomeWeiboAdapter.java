package com.shine.look.weibo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.Status;
import com.shine.look.weibo.ui.views.WeiboContentView;
import com.shine.look.weibo.utils.Utils;

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

    //截取新浪短网址
    //public final Pattern mVideoPattern = Pattern.compile("http://t.cn/[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]{5,8}[a-zA-Z0-9+&@#/%=~_|]");

    private static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private final LayoutInflater mInflater;
    private final Context mContext;

    private List<Status> mData;
    private boolean mAnimateItems = true;

    /**
     * 最后一个进行动画的item的位置
     */
    private int mLastAnimatedPosition = -1;

    private boolean isVisibleLoading;

    public HomeWeiboAdapter(Context context) {
        this.mData = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public HomeWeiboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View loadingView = mInflater.inflate(R.layout.layout_recyclerview_footer, parent, false);
            return new HomeWeiboViewHolder(loadingView);
        } else {
            View rootView = mInflater.inflate(R.layout.item_home_weibo, parent, false);
            HomeWeiboViewHolder holder = new HomeWeiboViewHolder(rootView);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(HomeWeiboViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        if (getItemViewType(position) != VIEW_TYPE_LOADING) {
            Status status = mData.get(position);
            //微博内容
            holder.flWeiboContent.setWeiboContent(status, false);

            //转发的微博
            if (status.retweeted_status != null) {
                holder.cvRetweetedContent.setVisibility(View.VISIBLE);
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

    public void closeAnimateItems() {
        if (this.mAnimateItems) {
            this.mAnimateItems = false;
        }
    }

    public long getMaxId() {
        return Long.parseLong(mData.get(mData.size() - 1).id);
    }


    public static class HomeWeiboViewHolder extends RecyclerView.ViewHolder {
        @Optional
        @InjectView(R.id.flWeiboContent)
        WeiboContentView flWeiboContent;
        @Optional
        @InjectView(R.id.cvRetweetedContent)
        CardView cvRetweetedContent;
        @Optional
        @InjectView(R.id.flRetweetedContent)
        WeiboContentView flRetweetedContent;
        @Optional
        @InjectView(R.id.tvAttitudesCount)
        TextView tvAttitudesCount;
        @Optional
        @InjectView(R.id.tvRepostsCount)
        TextView tvRepostsCount;
        @Optional
        @InjectView(R.id.tvCommentsCount)
        TextView tvCommentsCount;

        public HomeWeiboViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
