package com.shine.look.weibo.medel;

import com.shine.look.weibo.bean.HomeInfo;
import com.shine.look.weibo.http.RequestParams;
import com.shine.look.weibo.utils.Constants;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:获取当前登录用户及其所关注用户的最新微博
 */
public class HomeModel extends BaseModel {

    private int mPage = 3;

    @Override
    public void request() {
        if (mPage == 1) {
            setCache(true);
        }
        executeRequest(getRequest(HomeInfo.class));
    }

    @Override
    protected String getUrl() {
        RequestParams params = new RequestParams();
        params.setPath(Constants.URL_HOME_PATH);
        params.put(Constants.ARG_ACCESS_TOKEN, mAccessToken.getToken());
        params.put(Constants.ARG_PAGE, mPage);
        params.put(Constants.ARG_TRIM_USER, Constants.TRIM_USER);
        return params.getUrl();
    }

    public void setPage(int page) {
        mPage = page;
    }
}
