package com.shine.look.weibo.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.shine.look.weibo.http.RequestManager;

import butterknife.ButterKnife;

/**
 * User:Shine
 * Date:2015-05-03
 * Description:基类
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }
}
