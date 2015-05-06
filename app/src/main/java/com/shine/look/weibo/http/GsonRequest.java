/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.shine.look.weibo.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private final Gson mGson = new Gson();
    private final Class<T> mClazz;
    private final Listener<T> mListener;
    private final Map<String, String> mHeaders;
    private final Map<String, String> mParams;
    private final boolean mIsCache;

    /**
     * get方式请求，无缓冲
     *
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, false, clazz, null, null, listener, errorListener);
    }

    /**
     * post方式请求，无缓冲
     *
     * @param url
     * @param clazz
     * @param params
     * @param listener
     * @param errorListener
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) {
        this(Method.POST, url, false, clazz, null, params, listener, errorListener);
    }

    /**
     * get方式请求，根据isCache处理缓存
     *
     * @param url
     * @param isCache
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public GsonRequest(String url, boolean isCache, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, isCache, clazz, null, null, listener, errorListener);
    }

    /**
     * post方式请求,根据isCache处理缓存
     *
     * @param url
     * @param isCache
     * @param clazz
     * @param params
     * @param listener
     * @param errorListener
     */
    public GsonRequest(String url, boolean isCache, Class<T> clazz, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) {
        this(Method.POST, url, false, clazz, null, params, listener, errorListener);
    }

    public GsonRequest(int method, String url, boolean isCache, Class<T> clazz, Map<String, String> headers, Map<String, String> params,
                       Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mListener = listener;
        this.mParams = params;
        this.mIsCache = isCache;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams != null ? mParams : super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
