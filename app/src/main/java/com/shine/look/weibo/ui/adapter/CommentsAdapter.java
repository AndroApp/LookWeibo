package com.shine.look.weibo.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.Comment;
import com.shine.look.weibo.ui.views.ContentTextView;
import com.shine.look.weibo.ui.views.DateTextView;
import com.shine.look.weibo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-05-18
 * Description:
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private final RequestManager mGlide;
    private final LayoutInflater mInflater;
    private final int mAvatarSize;

    private List<Comment> mData;
    private Context mContext;
    private boolean mAnimationsLocked;
    private boolean isVisibleLoading;
    private int lastAnimatedPosition = -1;

    public CommentsAdapter(Context context, RequestManager requestManager) {
        this.mData = new ArrayList<>();
        this.mContext = context;
        this.mGlide = requestManager;
        this.mAvatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_LOADING) {
            View loadingView = mInflater.inflate(R.layout.layout_recyclerview_footer, parent, false);
            return new CommentsViewHolder(loadingView);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            return new CommentsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        if (getItemViewType(position) != Constants.VIEW_TYPE_LOADING) {
            Comment comment = mData.get(position);

            holder.tvUserName.setText(comment.user.name);
            holder.tvComment.dealWithText(comment.text);
            holder.tvCreatedTime.setTextByDate(comment.created_at);
            String avatarUrl = comment.user.avatar_large;
            if (avatarUrl.equals("")) {
                avatarUrl = comment.user.profile_image_url;
            }
            mGlide.load(avatarUrl)
                    .asBitmap()
                    .override(mAvatarSize, mAvatarSize)
                    .fitCenter()
                    .into(holder.ivUserAvatar);
        }
    }

    private void runEnterAnimation(View view, int position) {
        if (mAnimationsLocked) return;
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1)
                    .setStartDelay(20 * position)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mAnimationsLocked = true;
                        }
                    }).start();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

    public long getMaxId() {
        return Long.parseLong(mData.get(mData.size() - 1).id);
    }

    public void addItems(List<Comment> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {

        ImageView ivUserAvatar;
        ContentTextView tvComment;
        DateTextView tvCreatedTime;
        TextView tvUserName;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            ivUserAvatar = (ImageView) itemView.findViewById(R.id.ivUserAvatar);
            tvComment = (ContentTextView) itemView.findViewById(R.id.tvComment);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvCreatedTime = (DateTextView) itemView.findViewById(R.id.tvCreatedTime);
        }
    }
}
