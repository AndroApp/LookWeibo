package com.shine.look.weibo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        Status statusBean = mData.get(position);
        holder.tvUserName.setText(statusBean.user.screen_name);
        holder.tvCreatedTime.setTextByDate(statusBean.created_at);
        holder.tvSource.setText(Html.fromHtml(statusBean.source));
        holder.tvText.setContextText(statusBean.text);
        Picasso picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
        picasso.load(statusBean.user.profile_image_url)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .into(holder.ivUserProfile);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(List<Status> dataList) {
        mData.addAll(dataList);
        notifyDataSetChanged();
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
