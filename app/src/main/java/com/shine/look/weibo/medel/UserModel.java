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

    private int mUserId;
    private String mUserName;

    public UserModel(Activity activity) {
        super(activity);
    }

    public UserModel(Activity activity, int userId) {
        super(activity);
        this.mUserId = userId;
    }

    public UserModel(Activity activity, String userName) {
        super(activity);
        this.mUserName = userName;
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
        if (mUserId != 0) {
            params.put(Constants.ARG_USER_ID, mUserId);
        } else if (mUserName != null) {
            try {
                params.put(Constants.ARG_USER_NAME, URLEncoder.encode(mUserName, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            params.put(Constants.ARG_USER_ID, mAccessToken.getUid());
        }
        String url = params.getUrl();
        return url;
    }
}
