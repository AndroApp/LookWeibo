package com.shine.look.weibo.medel;

import android.app.Activity;

import com.shine.look.weibo.http.RequestParams;

/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class UrlShortModel extends BaseModel {

    private String mUrlShort;

    public UrlShortModel(Activity activity) {
        super(activity);
    }

    @Override
    public void request() {
//        executeStringRequest();
    }

    @Override
    protected String getUrl() {
        RequestParams params = new RequestParams();
        params.setPath("https://api.weibo.com/2/short_url/expand.json?");
        params.put("access_token", mAccessToken.getToken());
        params.put("url_short", mUrlShort);
        return params.getUrl();
    }

    public void setUrlShort(String urlShort) {
        mUrlShort = urlShort;
    }
}
