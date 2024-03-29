package com.shine.look.weibo.medel;

import android.app.Activity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.shine.look.weibo.bean.gson.ErrorInfo;
import com.shine.look.weibo.http.GsonRequest;
import com.shine.look.weibo.http.HttpError;
import com.shine.look.weibo.http.RequestManager;
import com.shine.look.weibo.utils.AccessTokenKeeper;
import com.shine.look.weibo.utils.FileHelper;
import com.shine.look.weibo.utils.LogHelper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.Map;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:
 */
public abstract class BaseModel implements Response.Listener<String> {

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

    private Class mClazz;

    public BaseModel(Activity activity) {
        this.isDiskCache = true;
        this.mAccessToken = AccessTokenKeeper.readAccessToken();
    }

    public abstract void request();

    protected void executeRequest(final Class clazz) {
        final String url = getUrl();
        mClazz = clazz;
        if (isDiskCache) {
            isDiskCache = false;
            String jsonStr = FileHelper.loadFile(url, mAccessToken.getUid());
            if (jsonStr != null && !jsonStr.equals("") && onRequestListener != null) {
                onRequestListener.onSuccess(mGson.fromJson(jsonStr, clazz));
                return;
            }
        }
        StringRequest request = new StringRequest(url, this, new Response.ErrorListener() {
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
        RequestManager.addRequest(request, null);
    }

    protected void executeRequest(Map<String, String> params, final Class clazz) {
        GsonRequest request = new GsonRequest(getUrl(), clazz, params, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                if (onRequestListener != null) {
                    onRequestListener.onSuccess(o);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (onRequestListener != null) {
                    onRequestListener.onFailure(volleyError);
                }
            }
        });

        RequestManager.addRequest(request, null);
    }


    @Override
    public void onResponse(String s) {
        if (isCache) {
            isCache = false;
            FileHelper.saveFile(getUrl(), mAccessToken.getUid(), s);
        }
        if (onRequestListener != null) {
            onRequestListener.onSuccess(mGson.fromJson(s, mClazz));
        }
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
