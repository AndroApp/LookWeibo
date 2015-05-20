package com.shine.look.weibo.medel;

import android.app.Activity;

import com.shine.look.weibo.bean.Comment;
import com.shine.look.weibo.http.RequestParams;
import com.shine.look.weibo.utils.Constants;
import com.shine.look.weibo.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * User:Shine
 * Date:2015-05-19
 * Description:
 */
public class WriteCommentModel extends BaseModel {

    private final String weiboId;
    private String text;

    public WriteCommentModel(Activity activity, String weiboId) {
        super(activity);
        this.weiboId = weiboId;
    }

    @Override
    public void request() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.ARG_ACCESS_TOKEN, mAccessToken.getToken());
        params.put(Constants.ARG_COMMENT, text);
        params.put(Constants.ARG_ID, weiboId);
        params.put(Constants.ARG_RID, Utils.getPsdnIp());

        executeRequest(params, Comment.class);
    }

    @Override
    protected String getUrl() {
        RequestParams params = new RequestParams();
        params.setPath(Constants.URL_WRITE_COMMENT_PATH);

        return params.getUrl();
    }

    public void setText(String text) {
        this.text = text;
    }
}
