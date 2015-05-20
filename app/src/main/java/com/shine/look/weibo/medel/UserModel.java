package com.shine.look.weibo.medel;

import android.app.Activity;

import com.shine.look.weibo.bean.User;
import com.shine.look.weibo.http.RequestParams;
import com.shine.look.weibo.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * User:Shine
 * Date:2015-05-11
 * Description:
 */
public class UserModel extends BaseModel {

    private String mUserId;
    private String mUserName;

    public UserModel(Activity activity, String userId, String userName) {
        super(activity);
        this.mUserName = userName;
        this.mUserId = userId;
        setDiskCache(false);
    }

    @Override
    public void request() {
        executeRequest(User.class);
    }

    @Override
    protected String getUrl() {
        RequestParams params = new RequestParams();

        params.setPath(Constants.URL_USER_PATH);
        params.put(Constants.ARG_ACCESS_TOKEN, mAccessToken.getToken());
        if (mUserName != null) {
            try {
                params.put(Constants.ARG_USER_NAME, URLEncoder.encode(mUserName, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (mUserId != null) {
            params.put(Constants.ARG_USER_ID, mUserId);
        } else {
            params.put(Constants.ARG_USER_ID, mAccessToken.getUid());
        }
        String url = params.getUrl();
        return url;
    }
}
