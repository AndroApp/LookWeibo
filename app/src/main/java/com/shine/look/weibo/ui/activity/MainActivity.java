package com.shine.look.weibo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.HomeInfo;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.HomeModel;
import com.shine.look.weibo.ui.adapter.HomeWeiboAdapter;

import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-03
 * Description:主页面
 */
public class MainActivity extends BaseActivity implements BaseModel.OnRequestListener<HomeInfo>, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.rvHome)
    RecyclerView rvHome;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    private HomeWeiboAdapter mAdapter;

    protected HomeModel mModel;

    private int mCurrentPage;

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
                swipeContainer.setRefreshing(false);
                if (info.total_number > 0 && info.statuses.size() > 0) {
                    if (mModel.getPage() == 1 && mAdapter.getItemCount() > 0) {
                        mAdapter.clear(mLastVisiblePosition);
                    }
                    mAdapter.addItems(info.statuses);
                }
            }
        }, 500);
    }

    @Override
    public void onFailure(VolleyError error) {
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (mModel != null) {
            mCurrentPage = 1;
            mLastVisiblePosition = ((LinearLayoutManager) rvHome.getLayoutManager()).findLastVisibleItemPosition();
            mModel.setPage(mCurrentPage);
            mModel.request();
        }

    }
}
