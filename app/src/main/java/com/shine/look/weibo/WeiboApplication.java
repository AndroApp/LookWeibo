package com.shine.look.weibo;

import android.app.Application;

import com.shine.look.weibo.http.RequestManager;


/**
 * User: shine
 * Date: 2015-04-28
 * Time: 09:53
 * Description:
 */
public class WeiboApplication extends Application {

    private static WeiboApplication sContent;

    public WeiboApplication() {
        sContent = this;
    }

    public static WeiboApplication getContext() {
        return sContent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.init(this);
    }

}
