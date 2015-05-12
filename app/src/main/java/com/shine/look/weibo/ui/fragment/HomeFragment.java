package com.shine.look.weibo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.HomeInfo;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.HomeModel;
import com.shine.look.weibo.ui.adapter.HomeWeiboAdapter;
import com.shine.look.weibo.ui.views.LoadMoreRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-11
 * Description:
 */
public class HomeFragment extends BaseFragment implements BaseModel.OnRequestListener<HomeInfo>, SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.OnRefreshEndListener {


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

        mAdapter = new HomeWeiboAdapter(getActivity());
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
}
