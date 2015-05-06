package com.shine.look.weibo.http;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User: shine
 * Date: 2015-04-30
 * Time: 09:04
 * Description:
 */
public class RequestParams {

    private StringBuilder mParamsString = new StringBuilder();
    protected final ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    private String mPath;

    public void setPath(String path) {
        this.mPath = path;
    }

    /**
     * Adds a int value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value int for the new param.
     */
    public void put(String key, Object value) {
        if (key != null) {
            urlParams.put(key, String.valueOf(value));
            mParamsString.append(String.valueOf(value));
            mParamsString.append("&");
        }
    }

    public String getUrl() {
        StringBuilder result = new StringBuilder(mPath);
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
            if (result.length() > 0)
                result.append("&");
        }
        return result.toString();
    }
}
