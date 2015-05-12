package com.shine.look.weibo;

import android.app.Application;
import android.content.Context;

import com.shine.look.weibo.http.RequestManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * User: shine
 * Date: 2015-04-28
 * Time: 09:53
 * Description:
 */
public class WeiboApplication extends Application {

    private static WeiboApplication sContent;

    public static RefWatcher getRefWatcher(Context context) {
        WeiboApplication application = (WeiboApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.init(this);
        refWatcher = LeakCanary.install(this);
    }

    public WeiboApplication() {
        sContent = this;
    }

    public static WeiboApplication getContext() {
        return sContent;
    }

}
