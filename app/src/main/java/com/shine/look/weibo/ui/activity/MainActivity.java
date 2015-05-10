package com.shine.look.weibo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.HomeInfo;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.HomeModel;
import com.shine.look.weibo.ui.adapter.HomeWeiboAdapter;
import com.shine.look.weibo.ui.views.LoadMoreRecyclerView;
import com.shine.look.weibo.utils.ToastHelper;

import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-03
 * Description:主页面
 */
public class MainActivity extends BaseActivity implements BaseModel.OnRequestListener<HomeInfo>, SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.OnRefreshEndListener {

    @InjectView(R.id.rvHome)
    LoadMoreRecyclerView rvHome;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    private HomeWeiboAdapter mAdapter;

    protected HomeModel mModel;

    private int mLastVisiblePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent);
        mAdapter = new HomeWeiboAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvHome.setLayoutManager(linearLayoutManager);
        rvHome.setAdapter(mAdapter);
        rvHome.setOnRefreshEndListener(this);

        mModel = new HomeModel(this);
        mModel.setOnRequestListener(this);
        mModel.request();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
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
                    ToastHelper.show(getString(R.string.sina_limit_only_load), R.mipmap.d_xiaoku);
                }
            }
        }, 500);
    }

    private void closeRefresh() {
        if (rvHome.isLoading()) {
            rvHome.setCloseLoading();
        }
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        closeRefresh();
    }

    @Override
    public void onRefresh() {
        if (mModel != null) {
            mLastVisiblePosition = ((LinearLayoutManager) rvHome.getLayoutManager()).findLastVisibleItemPosition();
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
