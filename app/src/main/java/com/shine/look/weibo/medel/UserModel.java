package com.shine.look.weibo.medel;

import android.app.Activity;

import com.shine.look.weibo.http.RequestParams;
import com.shine.look.weibo.utils.Constants;
import com.shine.weibosdk.openapi.models.User;

/**
 * User:Shine
 * Date:2015-05-11
 * Description:
 */
public class UserModel extends BaseModel {

    public UserModel(Activity activity) {
        super(activity);
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
        params.put(Constants.ARG_USER_ID, mAccessToken.getUid());
        return params.getUrl();
    }
}
