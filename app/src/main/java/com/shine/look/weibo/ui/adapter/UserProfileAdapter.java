package com.shine.look.weibo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.Status;
import com.shine.look.weibo.bean.User;
import com.shine.look.weibo.ui.views.WeiboContentView;
import com.shine.look.weibo.utils.AccessTokenKeeper;
import com.shine.look.weibo.utils.Utils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User:Shine
 * Date:2015-05-20
 * Description:
 */
public class UserProfileAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    public static final int TYPE_PROFILE_HEADER = 0;
    public static final int TYPE_PROFILE_WEIBO = 1;

    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private final LayoutInflater mInflater;
    private final RequestManager mGlide;
    private final int mAvatarSize;
    private final Oauth2AccessToken mAccessToken;


    private User mUser;
    private List<Status> mData;
    private HomeWeiboAdapter.OnFeedItemClickListener mOnFeedItemClickListener;
    private ThumbnailPicAdapter.OnThumbnailPicListener mOnThumbnailPicListener;
    private WeiboContentView.OnPictureListener mOnPictureListener;

    private boolean mLockedAnimations = false;
    private boolean mAnimateItems = true;
    private int mLastAnimatedPosition = -1;

    public UserProfileAdapter(Context context, RequestManager requestManager) {
        this.mData = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.mGlide = requestManager;
        this.mAvatarSize = context.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        this.mAccessToken = AccessTokenKeeper.readAccessToken();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PROFILE_HEADER) {
            View view = mInflater.inflate(R.layout.layout_user_profile_header, parent, false);
            return new ProfileHeaderViewHolder(view);
        } else {
            View rootView = mInflater.inflate(R.layout.item_home_weibo, parent, false);
            HomeWeiboAdapter.HomeWeiboViewHolder holder = new HomeWeiboAdapter.HomeWeiboViewHolder(rootView);
            holder.flWeiboContent.setGlide(mGlide);
            holder.flWeiboContent.setOnPictureListener(mOnPictureListener);
            holder.flRetweetedContent.setGlide(mGlide);
            holder.flRetweetedContent.setOnPictureListener(mOnPictureListener);
            holder.flWeiboContent.setOnThumbnailPicListener(mOnThumbnailPicListener);
            holder.flRetweetedContent.setOnThumbnailPicListener(mOnThumbnailPicListener);
            holder.btnReposts.setOnClickListener(this);
            holder.btnComments.setOnClickListener(this);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PROFILE_HEADER) {
            bindProfileHeader((ProfileHeaderViewHolder) holder);
        } else {
            bindProfileWeibo((HomeWeiboAdapter.HomeWeiboViewHolder) holder, position);
        }
    }

    private void bindProfileHeader(final ProfileHeaderViewHolder holder) {
        if (mUser == null) {
            return;
        }
        if (mUser.idstr.equals(mAccessToken.getUid())) {
            holder.btnFollow.setVisibility(View.GONE);
        }
        String picUrl = mUser.avatar_hd;
        if (picUrl.equals("")) {
            picUrl = mUser.profile_image_url;
        }
        mGlide.load(picUrl)
                .asBitmap()
                .override(mAvatarSize, mAvatarSize)
                .into(holder.ivUserProfilePhoto);
        holder.tvUserProfileName.setText(mUser.name);
        holder.tvWeiboNum.setText(mUser.statuses_count + "");
        holder.tvFansNum.setText(mUser.followers_count + "");
        holder.tvAttentionNum.setText(mUser.friends_count + "");

        Date date = new Date(mUser.created_at);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.tvCreatedTime.setText(sdf.format(date));
        if (!mUser.verified_reason.equals("")) {
            holder.tvUserProfileInfo.setText(mUser.verified_reason);
        } else if (!mUser.description.equals("")) {
            holder.tvUserProfileInfo.setText(mUser.description);
        }

        if (mUser.cover_image_phone != null && !mUser.equals("")) {

        }

        holder.vUserProfileRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.vUserProfileRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                animateUserProfileHeader(holder);
                return false;
            }
        });

    }

    private void animateUserProfileHeader(ProfileHeaderViewHolder viewHolder) {
        if (!mLockedAnimations) {
            mLockedAnimations = true;

            viewHolder.vUserProfileRoot.setTranslationY(-viewHolder.vUserProfileRoot.getHeight());
            viewHolder.ivUserProfilePhoto.setTranslationY(-viewHolder.ivUserProfilePhoto.getHeight());
            viewHolder.vUserDetails.setTranslationY(-viewHolder.vUserDetails.getHeight());
            viewHolder.vUserStats.setAlpha(0);

            viewHolder.vUserProfileRoot.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
            viewHolder.ivUserProfilePhoto.animate().translationY(0).setDuration(300).setStartDelay(100).setInterpolator(INTERPOLATOR);
            viewHolder.vUserDetails.animate().translationY(0).setDuration(300).setStartDelay(200).setInterpolator(INTERPOLATOR);
            viewHolder.vUserStats.animate().alpha(1).setDuration(200).setStartDelay(400).setInterpolator(INTERPOLATOR).start();
        }
    }

    private void bindProfileWeibo(HomeWeiboAdapter.HomeWeiboViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.btnReposts.setTag(position - 1);
        holder.btnComments.setTag(position - 1);
        Status status = mData.get(position - 1);
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
        return mData.size() + (mUser == null ? 0 : 1);
    }

    public void addUser(User user) {
        this.mUser = user;
        notifyDataSetChanged();
    }

    public void addItems(List<Status> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_PROFILE_HEADER;
        } else {
            return TYPE_PROFILE_WEIBO;
        }
    }

    public void closeAnimateItems() {
        if (this.mAnimateItems) {
            this.mAnimateItems = false;
        }
    }

    public void setOnThumbnailPicListener(ThumbnailPicAdapter.OnThumbnailPicListener listener) {
        mOnThumbnailPicListener = listener;
    }

    public void setOnFeedItemClickListener(HomeWeiboAdapter.OnFeedItemClickListener listener) {
        this.mOnFeedItemClickListener = listener;
    }

    public void setOnPictureListener(WeiboContentView.OnPictureListener onPictureListener) {
        mOnPictureListener = onPictureListener;
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
                    mOnFeedItemClickListener.onCommentsClick(v, mData.get((Integer) v.getTag()).idstr);
                }
                break;
        }
    }

    public static class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {
        View vUserProfileRoot;
        ImageView ivUserProfilePhoto;
        TextView tvUserProfileName;
        TextView tvUserProfileInfo;
        TextView tvWeiboNum;
        TextView tvFansNum;
        TextView tvAttentionNum;
        TextView tvCreatedTime;
        Button btnFollow;
        View vUserStats;
        View vUserDetails;

        public ProfileHeaderViewHolder(View itemView) {
            super(itemView);
            ivUserProfilePhoto = (ImageView) itemView.findViewById(R.id.ivUserProfilePhoto);
            tvUserProfileName = (TextView) itemView.findViewById(R.id.tvUserProfileName);
            tvUserProfileInfo = (TextView) itemView.findViewById(R.id.tvUserProfileInfo);
            tvWeiboNum = (TextView) itemView.findViewById(R.id.tvWeiboNum);
            tvFansNum = (TextView) itemView.findViewById(R.id.tvFansNum);
            tvAttentionNum = (TextView) itemView.findViewById(R.id.tvAttentionNum);
            tvCreatedTime = (TextView) itemView.findViewById(R.id.tvCreatedTime);
            btnFollow = (Button) itemView.findViewById(R.id.btnFollow);
            vUserProfileRoot = itemView.findViewById(R.id.vUserProfileRoot);
            vUserStats = itemView.findViewById(R.id.vUserStats);
            vUserDetails = itemView.findViewById(R.id.vUserDetails);
        }
    }

}
