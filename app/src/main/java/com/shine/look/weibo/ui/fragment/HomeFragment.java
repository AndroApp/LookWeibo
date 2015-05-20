package com.shine.look.weibo.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.shine.look.weibo.bean.ThumbnailPic;
import com.shine.look.weibo.bean.gson.HomeInfo;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.HomeModel;
import com.shine.look.weibo.ui.activity.BaseActivity;
import com.shine.look.weibo.ui.activity.CommentsActivity;
import com.shine.look.weibo.ui.activity.MainActivity;
import com.shine.look.weibo.ui.activity.ShowPictureActivity;
import com.shine.look.weibo.ui.activity.UserProfileActivity;
import com.shine.look.weibo.ui.adapter.HomeWeiboAdapter;
import com.shine.look.weibo.ui.adapter.ThumbnailPicAdapter;
import com.shine.look.weibo.ui.utils.AnimationUtils;
import com.shine.look.weibo.ui.views.LoadMoreRecyclerView;
import com.shine.look.weibo.ui.views.WeiboContentView;

import java.util.ArrayList;

/**
 * User:Shine
 * Date:2015-05-11
 * Description:
 */
public class HomeFragment extends BaseFragment implements BaseModel.OnRequestListener<HomeInfo>
        , SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.OnRefreshEndListener
        , WeiboContentView.OnPictureListener, ThumbnailPicAdapter.OnThumbnailPicListener, View.OnClickListener
        , HomeWeiboAdapter.OnFeedItemClickListener {


    private HomeWeiboAdapter mAdapter;

    protected HomeModel mModel;

    private LoadMoreRecyclerView mRvHome;
    private SwipeRefreshLayout mSwipeContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvHome = (LoadMoreRecyclerView) view.findViewById(R.id.rvHome);
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        init();
    }


    private void init() {
        mSwipeContainer.setOnRefreshListener(this);
        mSwipeContainer.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent);

        RequestManager requestManager = Glide.with(getActivity().getApplicationContext());
        mAdapter = new HomeWeiboAdapter(getActivity(), requestManager);
        mAdapter.setOnThumbnailPicListener(this);
        mAdapter.setOnFeedItemClickListener(this);
        mAdapter.setOnPictureListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRvHome.setLayoutManager(linearLayoutManager);
        mRvHome.setAdapter(mAdapter);
        mRvHome.setOnRefreshEndListener(this);

        mModel = new HomeModel(getActivity());
        mModel.setOnRequestListener(this);
        mModel.request();

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setInboxMenuItemOnClick(this);
        }

    }


    @Override
    public void onSuccess(final HomeInfo info) {
        closeRefresh();
        if (info.statuses != null && info.statuses.size() > 0 && info.total_number > 0) {
            if (mModel.getMaxId() == 0 && mAdapter.getItemCount() > 0) {
                mAdapter.clear();
            }
            mAdapter.addItems(info.statuses);
        }
        if (mAdapter.getItemCount() == info.total_number || info.statuses.size() == 0) {
            mRvHome.setLoading(true);
        }
//        else if (info.total_number >= mAdapter.getItemCount()) {
//            //ToastHelper.show(getString(R.string.sina_limit_only_load), R.drawable.d_xiaoku);
//        }
    }

    private void closeRefresh() {
        if (mRvHome.isLoading()) {
            mRvHome.setLoading(false);
            mAdapter.removeLoading();
        }
        if (mSwipeContainer.isRefreshing()) {
            mSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInbox:
                AnimationUtils.moveRecyclerViewToTop(mRvHome);
                break;
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        closeRefresh();
    }

    @Override
    public void onRefresh() {
        if (mModel != null) {
            mModel.setMaxId(0);
            mModel.request();
        }

    }

    @Override
    public void onEnd() {
        if (mModel != null) {
            mAdapter.addLoading();
            mModel.setMaxId(mAdapter.getMaxId() - 1);
            mModel.request();
        }
    }

    @Override
    public void onPictureClick(View v) {
        ImageView imageView = (ImageView) v;
        String picUrl = (String) imageView.getTag();
        Drawable drawable = imageView.getDrawable();
        ShowPictureActivity.start(getActivity(), imageView, drawable, picUrl);
    }

    @Override
    public void onHeaderPicClick(View v) {
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        startingLocation[0] += v.getWidth() / 2;
        UserProfileActivity.start(getActivity(), (String) v.getTag(), ((BaseActivity) getActivity()).toolBarIsMenu(), ((BaseActivity) getActivity()).toolBarMenuResId(), startingLocation);
    }

    @Override
    public void onThumbnailPicClick(View v, int position, ArrayList<ThumbnailPic> data) {
        ImageView imageView = (ImageView) v;
        Drawable drawable = imageView.getDrawable();
        ShowPictureActivity.start(getActivity(), imageView, drawable, data, position);
    }

    @Override
    public void onCommentsClick(View view, String weiboId) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        CommentsActivity.start(getActivity(), screenLocation[1], weiboId, ((BaseActivity) getActivity()).toolBarIsMenu(), ((BaseActivity) getActivity()).toolBarMenuResId());

    }

    @Override
    public void onRepostsClick(View view, int position) {

    }

}
