package com.shine.look.weibo.ui.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.User;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.UserModel;
import com.shine.look.weibo.ui.adapter.NavigationAdapter;

/**
 * User:Shine
 * Date:2015-05-11
 * Description:
 */
public class NavigationMenuView extends ListView implements View.OnClickListener, BaseModel.OnRequestListener<User> {

    private OnHeaderClickListener mHeaderClickListener;

    private ImageView mIvUserProfilePhoto;
    private TextView mTvUserName;

    private int mAvatarSize;

    public NavigationMenuView(Context context) {
        super(context);
        init();
    }

    public NavigationMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        //让ListView进入选择模式
        setChoiceMode(CHOICE_MODE_SINGLE);
        setDivider(getResources().getDrawable(android.R.color.transparent));
        setDividerHeight(0);
        setBackgroundColor(Color.WHITE);

        setUpHeight();
        setUpAdapter();
        setUserPhoto();
    }

    private void setUpHeight() {
        mAvatarSize = getResources().getDimensionPixelSize(R.dimen.navigation_menu_avatar_size);
        setHeaderDividersEnabled(true);
        View vHeader = LayoutInflater.from(getContext()).inflate(R.layout.layout_navigation_menu_header, null);
        mIvUserProfilePhoto = (ImageView) vHeader.findViewById(R.id.ivUserProfilePhoto);
        mTvUserName = (TextView) vHeader.findViewById(R.id.tvUserName);
        addHeaderView(vHeader);
        vHeader.setOnClickListener(this);
    }

    private void setUpAdapter() {
        NavigationAdapter adapter = new NavigationAdapter(getContext());
        setAdapter(adapter);
    }

    private void setUserPhoto() {
        UserModel model = new UserModel((Activity) getContext(), null, null);
        model.setOnRequestListener(this);
        model.setCache(true);
        model.request();
    }

    @Override
    public void onSuccess(User user) {
        if (mIvUserProfilePhoto != null) {
            String avatarUrl = user.avatar_large != null && !user.avatar_large.equals("") ? user.avatar_large : user.profile_url;
            Glide.with(getContext())
                    .load(avatarUrl)
                    .override(mAvatarSize, mAvatarSize)
                    .fitCenter()
                    .into(mIvUserProfilePhoto);
            mIvUserProfilePhoto.setOnClickListener(this);
        }
        if (mTvUserName != null) {
            mTvUserName.setText(user.screen_name);
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }

    @Override
    public void onClick(View v) {
        if (mHeaderClickListener != null) {
            mHeaderClickListener.onHeaderClick(v);
        }
    }

    public void setOnHeaderClickListener(OnHeaderClickListener headerClickListener) {
        mHeaderClickListener = headerClickListener;
    }


    public interface OnHeaderClickListener {
        public void onHeaderClick(View v);
    }
}
