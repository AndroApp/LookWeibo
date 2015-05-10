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

    public static final String URL_THUMBNAIL_PATH = "/thumbnail/";
    public static final String URL_BMIDDLE_PATH = "/bmiddle/";
    public static final String URL_LARGE_PATH = "/large/";


    /*
     *接口传递参数名
     */
    public static final String ARG_ACCESS_TOKEN = "access_token";    //账号token
    public static final String ARG_COUNT = "count";                 //页记录条数
    public static final String ARG_MAX_ID = "max_id";              //指定获取的微博范围

    /*
     *接口传递的参数
     */
    public static final int PAGE_COUNT = 40;

}

