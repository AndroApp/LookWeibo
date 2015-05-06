package com.shine.look.weibo.medel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.shine.look.weibo.http.RequestManager;
import com.shine.look.weibo.utils.AccessTokenKeeper;
import com.shine.look.weibo.utils.FileHelper;
import com.shine.look.weibo.utils.LogHelper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:
 */
public abstract class BaseModel {

    private static final String TAG = LogHelper.makeLogTag(BaseModel.class);

    /**
     * 微博账号Token
     */
    protected final Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken();

    private final Gson mGson = new Gson();

    protected OnRequestListener onRequestListener;

    /**
     * 是否缓存,只进行一次数据存储
     */
    private boolean isCache;

    public abstract void request();

    public StringRequest getRequest(final Class clazz) {
        String jsonStr = FileHelper.loadFile(getUrl());
        if (jsonStr != null && !jsonStr.equals("")) {
            onRequestListener.onSuccess(mGson.fromJson(jsonStr, clazz));
            return null;
        } else {
            return new StringRequest(getUrl(), new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    if (isCache) {
                        isCache = false;
                        FileHelper.saveFile(getUrl(), json);
                    }
                    if (onRequestListener != null) {
                        onRequestListener.onSuccess(mGson.fromJson(json, clazz));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogHelper.d(TAG, volleyError.toString());
                    if (onRequestListener != null) {
                        onRequestListener.onFailure(volleyError);
                    }
                }
            });
        }
    }


    /**
     * @return 请求的url，不能返回null
     */
    protected abstract String getUrl();

    protected void executeRequest(Request<?> request) {
        if (request != null) {
            RequestManager.addRequest(request, this);
        }
    }

    public void setOnRequestListener(OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
    }

    public interface OnRequestListener<T> {
        public void onSuccess(T info);

        public void onFailure(VolleyError error);
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean isCache) {
        this.isCache = isCache;
    }
}
