package com.shine.look.weibo.medel;

import android.app.Activity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.shine.look.weibo.bean.ErrorInfo;
import com.shine.look.weibo.http.HttpError;
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
    protected final Oauth2AccessToken mAccessToken;

    private final Gson mGson = new Gson();

    protected OnRequestListener onRequestListener;

    /**
     * 是否缓存,只进行一次数据存储
     */
    private boolean isCache;

    private boolean isDiskCache;

    private Activity mActivity;

    public BaseModel(Activity activity) {
        this.mActivity = activity;
        this.isDiskCache = true;
        this.mAccessToken = AccessTokenKeeper.readAccessToken();
    }

    public abstract void request();

    protected void executeRequest(final Class clazz) {
        final String url = getUrl();
        if (isDiskCache) {
            isDiskCache = false;
            String jsonStr = FileHelper.loadFile(url, mAccessToken.getUid());
            if (jsonStr != null && !jsonStr.equals("") && onRequestListener != null) {
                onRequestListener.onSuccess(mGson.fromJson(jsonStr, clazz));
                return;
            }
        }
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                if (isCache) {
                    isCache = false;
                    FileHelper.saveFile(url, mAccessToken.getUid(), json);
                }
                if (onRequestListener != null) {
                    onRequestListener.onSuccess(mGson.fromJson(json, clazz));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    String errorString = new String(volleyError.networkResponse.data);
                    HttpError.showError(mGson.fromJson(errorString, ErrorInfo.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (onRequestListener != null) {
                    onRequestListener.onFailure(volleyError);
                }
            }
        });
        RequestManager.addRequest(request, mActivity);
    }


    /**
     * @return 请求的url，不能返回null
     */
    protected abstract String getUrl();

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

    public boolean isDiskCache() {
        return isDiskCache;
    }

    public void setDiskCache(boolean isDiskCache) {
        this.isDiskCache = isDiskCache;
    }

}
