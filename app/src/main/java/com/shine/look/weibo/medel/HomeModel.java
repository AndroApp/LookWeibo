package com.shine.look.weibo.medel;

import android.app.Activity;

import com.shine.look.weibo.bean.HomeInfo;
import com.shine.look.weibo.http.RequestParams;
import com.shine.look.weibo.utils.Constants;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:获取当前登录用户及其所关注用户的最新微博
 */
public class HomeModel extends BaseModel {

    private long mMaxId;

    public HomeModel(Activity activity) {
        super(activity);
    }

    @Override
    public void request() {
        if (mMaxId == 0) {
            setCache(true);
        }
        executeRequest(HomeInfo.class);
    }

    @Override
    protected String getUrl() {
        RequestParams params = new RequestParams();
        params.setPath(Constants.URL_HOME_PATH);
        params.put(Constants.ARG_ACCESS_TOKEN, mAccessToken.getToken());
        params.put(Constants.ARG_MAX_ID, getMaxId());
        params.put(Constants.ARG_COUNT, Constants.PAGE_COUNT);
        return params.getUrl();
    }

    public long getMaxId() {
        return mMaxId;
    }

    public void setMaxId(long maxId) {
        mMaxId = maxId;
    }
}
