package com.shine.look.weibo.utils;

/**
 * User: shine
 * Date: 2015-04-30
 * Time: 12:12
 * Description:常量类
 */
public class Constants {
    //应用的appkey
    public static final String APP_KEY = "444702152";
    //应用的回调页
    public static final String REDIRECT_URL = "http://www.sina.com";
    // 应用申请的高级权限
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * 微博接口url路径
     */
    public static final String URL_SINAL_SHORT = "http://t.cn/";    //新浪短域名
    public static final String URL_HOME_PATH = "https://api.weibo.com/2/statuses/friends_timeline.json?";    //首页路径
    public static final String URL_COMMENTS_PATH = "https://api.weibo.com/2/comments/show.json?";    //获取微博评论路径
    public static final String URL_USER_PATH = "https://api.weibo.com/2/users/show.json?";    //查询用户信息路径
    public static final String URL_WRITE_COMMENT_PATH = "https://api.weibo.com/2/comments/create.json?";    //对某微博评论路径
    public static final String URL_USER_TIMELINE_PATH = "https://api.weibo.com/2/statuses/user_timeline.json?";    //对某微博评论路径


    public static final String URL_THUMBNAIL_PATH = "/thumbnail/";
    public static final String URL_BMIDDLE_PATH = "/bmiddle/";
    public static final String URL_LARGE_PATH = "/large/";


    /*
     *接口传递参数名
     */
    public static final String ARG_ACCESS_TOKEN = "access_token";    //账号token
    public static final String ARG_COUNT = "count";                 //页记录条数
    public static final String ARG_MAX_ID = "max_id";              //指定获取的微博范围
    public static final String ARG_USER_ID = "uid";              //用户id
    public static final String ARG_USER_NAME = "screen_name";              //用户名称
    public static final String ARG_ID = "id";
    public static final String ARG_RID = "rid";
    public static final String ARG_COMMENT = "comment";

    /*
     *接口传递的参数
     */
    public static final int PAGE_COUNT = 20;


    public static final String ARG_TOOLBAR_IS_MENU = "com.shine.look.weibo.isMenu";
    public static final String ARG_TOOLBAR_MENU_RES_ID = "com.shine.look.weibo.menuResID";
    public static final String ARG_WEIBO_ID = "com.shine.look.weibo.weiboId";
    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADING = 2;
}

