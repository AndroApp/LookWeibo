package com.shine.look.weibo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.HomeInfo;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.HomeModel;
import com.shine.look.weibo.ui.adapter.HomeWeiboAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-03
 * Description:主页面
 */
public class MainActivity extends BaseActivity implements BaseModel.OnRequestListener<HomeInfo> {

    @InjectView(R.id.rvHome)
    RecyclerView rvHome;

    private HomeWeiboAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        mAdapter = new HomeWeiboAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvHome.setLayoutManager(linearLayoutManager);
        rvHome.setAdapter(mAdapter);

        mModel = new HomeModel();
        mModel.setOnRequestListener(this);
        mModel.request();
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onSuccess(HomeInfo info) {
        if (info.total_number > 0 && info.statuses.size() > 0) {
            mAdapter.addItems(info.statuses);
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
