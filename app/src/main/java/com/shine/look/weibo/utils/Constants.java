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

    public static final long MINUTES_IN_MILLIS = 1000 * 60;//一分钟的毫秒数
    public static final long HOUR_IN_MILLIS = MINUTES_IN_MILLIS * 60;//一小时的毫秒数
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;//一天的毫秒数

    /**
     * 微博接口url路径
     */
    public static final String URL_HOME_PATH = "https://api.weibo.com/2/statuses/home_timeline.json?";    //首页路径
    public static final String URL_EMOTIONS_PATH = "https://api.weibo.com/2/emotions.json?";    //首页路径

    /*
     *接口传递参数名
     */
    public static final String ARG_ACCESS_TOKEN = "access_token";    //账号token
    public static final String ARG_COUNT = "count";                 //页记录条数
    public static final String ARG_PAGE = "page";                   //页码
    public static final String ARG_MAX_ID = "max_id";              //指定获取的微博范围

    /*
     *接口传递的参数
     */
    public static final int PAGE_COUNT = 40;

}

