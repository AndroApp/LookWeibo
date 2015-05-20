package com.shine.look.weibo.medel;

import android.app.Activity;

import com.shine.look.weibo.bean.gson.CommentsInfo;
import com.shine.look.weibo.http.RequestParams;
import com.shine.look.weibo.utils.Constants;

/**
 * User:Shine
 * Date:2015-05-18
 * Description:
 */
public class CommentsModel extends BaseModel {

    private final String mId;
    private long mMaxId;

    public CommentsModel(Activity activity, String id) {
        super(activity);
        this.mMaxId = 0;
        this.mId = id;
        setDiskCache(false);
    }


    @Override
    public void request() {
        executeRequest(CommentsInfo.class);
    }


    @Override
    protected String getUrl() {
        RequestParams params = new RequestParams();
        params.setPath(Constants.URL_COMMENTS_PATH);
        params.put(Constants.ARG_ACCESS_TOKEN, mAccessToken.getToken());
        params.put(Constants.ARG_ID, mId);
        params.put(Constants.ARG_MAX_ID, mMaxId);
        params.put(Constants.ARG_COUNT, Constants.PAGE_COUNT);
        String url = params.getUrl();
        return url;
    }

    public void setMaxId(long maxId) {
        mMaxId = maxId;
    }

    public long getMaxId() {
        return mMaxId;
    }
}
