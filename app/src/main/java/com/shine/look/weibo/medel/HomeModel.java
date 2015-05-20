package com.shine.look.weibo.medel;

import android.app.Activity;

import com.shine.look.weibo.bean.gson.HomeInfo;
import com.shine.look.weibo.http.RequestParams;
import com.shine.look.weibo.utils.Constants;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:获取当前登录用户及其所关注用户的最新微博
 */
public class HomeModel extends BaseModel {

    private long mMaxId;
    private String mPath;
    private String mUid;

    public HomeModel(Activity activity) {
        super(activity);
    }

    @Override
    public void request() {
        if (mMaxId == 0 && mUid == null) {
            setCache(true);
        } else {
            setCache(false);
            setDiskCache(false);
        }
        executeRequest(HomeInfo.class);
    }

    @Override
    protected String getUrl() {
        RequestParams params = new RequestParams();
        params.setPath(mPath == null ? Constants.URL_HOME_PATH : mPath);
        params.put(Constants.ARG_ACCESS_TOKEN, mAccessToken.getToken());
        params.put(Constants.ARG_MAX_ID, getMaxId());
        params.put(Constants.ARG_COUNT, Constants.PAGE_COUNT);
        if (mUid != null) {
            params.put(Constants.ARG_USER_ID, mUid);
        }
        String s = params.getUrl();
        return s;
    }

    public long getMaxId() {
        return mMaxId;
    }

    public void setMaxId(long maxId) {
        mMaxId = maxId;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public void setUid(String uid) {
        mUid = uid;
    }
}
