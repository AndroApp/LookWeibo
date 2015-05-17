package com.shine.look.weibo.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.HomeInfo;
import com.shine.look.weibo.bean.ThumbnailPic;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.HomeModel;
import com.shine.look.weibo.ui.activity.MainActivity;
import com.shine.look.weibo.ui.activity.ShowPictureActivity;
import com.shine.look.weibo.ui.adapter.HomeWeiboAdapter;
import com.shine.look.weibo.ui.adapter.ThumbnailPicAdapter;
import com.shine.look.weibo.ui.transition.ActivityTransitionLauncher;
import com.shine.look.weibo.ui.views.LoadMoreRecyclerView;
import com.shine.look.weibo.ui.views.WeiboContentView;
import com.shine.look.weibo.utils.Utils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-11
 * Description:
 */
public class HomeFragment extends BaseFragment implements BaseModel.OnRequestListener<HomeInfo>
        , SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.OnRefreshEndListener
        , WeiboContentView.OnPictureListener, ThumbnailPicAdapter.OnThumbnailPicListener {

    public static final String ARG_PICTURE_URL = "com.shine.look.weibo.picUrl";
    public static final String ARG_PICTURE_POSITION = "com.shine.look.weibo.picPosition";
    public static final String ARG_PICTURE_LIST_URL = "com.shine.look.weibo.list_picUrl";

    private HomeWeiboAdapter mAdapter;

    protected HomeModel mModel;

    private int mLastVisiblePosition;


    @InjectView(R.id.rvHome)
    LoadMoreRecyclerView mRvHome;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        init();
    }


    private void init() {
        mSwipeContainer.setOnRefreshListener(this);
        mSwipeContainer.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent);

        RequestManager requestManager = Glide.with(this);
        mAdapter = new HomeWeiboAdapter(getActivity(), requestManager, this);
        mAdapter.setOnThumbnailPicListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRvHome.setLayoutManager(linearLayoutManager);
        mRvHome.setAdapter(mAdapter);
        mRvHome.setOnRefreshEndListener(this);

        mModel = new HomeModel(getActivity());
        mModel.setOnRequestListener(this);
        mModel.request();

    }

    @Override
    public void onSuccess(final HomeInfo info) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeRefresh();
                if (info.total_number > 0 && info.statuses.size() > 0) {
                    if (mModel.getMaxId() == 0 && mAdapter.getItemCount() > 0) {
                        mAdapter.clear(mLastVisiblePosition);
                    }
                    mAdapter.addItems(info.statuses);
                } else if (info.total_number >= mAdapter.getItemCount()) {
                    //ToastHelper.show(getString(R.string.sina_limit_only_load), R.drawable.d_xiaoku);
                }
            }
        }, 500);
    }

    private void closeRefresh() {
        if (mRvHome.isLoading()) {
            mRvHome.setCloseLoading();
        }
        if (mSwipeContainer.isRefreshing()) {
            mSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        closeRefresh();
    }

    @Override
    public void onRefresh() {
        if (mModel != null) {
            mLastVisiblePosition = ((LinearLayoutManager) mRvHome.getLayoutManager()).findLastVisibleItemPosition();
            mModel.setMaxId(0);
            mModel.request();
        }

    }

    @Override
    public void onEnd() {
        if (mModel != null) {
            mModel.setMaxId(mAdapter.getMaxId() - 1);
            mModel.request();
        }
    }

    @Override
    public void onPictureClick(View v) {
        ImageView imageView = (ImageView) v;
        String picUrl = (String) imageView.getTag();
        Drawable drawable = imageView.getDrawable();
        final Intent intent = new Intent(getActivity(), ShowPictureActivity.class);
        intent.putExtra(ARG_PICTURE_URL, picUrl);
        ActivityTransitionLauncher.with(getActivity()).from(imageView).image(Utils.drawableToBitmap(drawable)).launch(intent);
        ((MainActivity) getActivity()).setTaskTop(true);
    }

    @Override
    public void onThumbnailPicClick(View v, int position, ArrayList<ThumbnailPic> data) {
        ImageView imageView = (ImageView) v;
        Drawable drawable = imageView.getDrawable();
        final Intent intent = new Intent(getActivity(), ShowPictureActivity.class);
        intent.putExtra(ARG_PICTURE_POSITION, position);
        intent.putParcelableArrayListExtra(ARG_PICTURE_LIST_URL, data);
        if (drawable == null) {
            startActivity(intent);
        } else {
            ActivityTransitionLauncher.with(getActivity()).from(imageView).image(Utils.drawableToBitmap(drawable)).launch(intent);
            ((MainActivity) getActivity()).setTaskTop(true);
        }
    }
}
