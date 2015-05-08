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
import com.shine.look.weibo.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:
 */
public class HomeWeiboAdapter extends RecyclerView.Adapter<HomeWeiboAdapter.HomeWeiboViewHolder> {

    private List<Status> mData;

    private Context mContext;

    private final int avatarSize;

    /**
     * 最后一个进行动画的item的位置
     */
    private int mLastAnimatedPosition = -1;

    public HomeWeiboAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
        avatarSize = Utils.dpToPx(50);
    }

    @Override
    public HomeWeiboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_home_weibo, parent, false);
        return new HomeWeiboViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(HomeWeiboViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
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
        holder.tvText.setContextText(statusBean.text);
        Picasso picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
        picasso.load(statusBean.user.profile_image_url)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .into(holder.ivUserProfile);
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

    class HomeWeiboViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ivUserProfile)
        ImageView ivUserProfile;
        @InjectView(R.id.tvUserName)
        TextView tvUserName;
        @InjectView(R.id.tvSource)
        TextView tvSource;
        @InjectView(R.id.tvCreatedTime)
        DateTextView tvCreatedTime;
        @InjectView(R.id.tvText)
        ContextTextView tvText;


        public HomeWeiboViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
