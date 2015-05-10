package com.shine.look.weibo.http;

import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.ErrorInfo;
import com.shine.look.weibo.utils.ToastHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class HttpError {


    public static final int SYSTEM_EROR = 10001;
    public static final int SYSTEM_UNAVAILBALE = 10002;
    public static final int REMOTE_SERVICE_ERROR = 10003;
    public static final int IP_LIMIT = 10004;
    public static final int PERMISSION_DENIED = 10005;
    public static final int SOURCE_PARAMTER = 10006;
    public static final int UNSIPPORT_MEDIA_TYPE = 10007;
    public static final int PARAM_ERROR = 10008;
    public static final int SYSTEM_IS_BUSY = 10009;
    public static final int JOB_EXPIRED = 10010;
    public static final int RPC_ERROR = 10011;
    public static final int ILLEGAL_REQUEST = 10012;
    public static final int INVALID_WEIBO_USER = 10013;
    public static final int INSUFFICIENT_APP_PERMISSIONS = 10014;
    public static final int MISS_REQUEIRED_PARAMETER = 10016;
    public static final int PARAMETER_VALUE_INVALID = 10017;
    public static final int REQUEST_LENGTH_LIMIT = 10018;
    public static final int API_NOT_FOUND = 10020;
    public static final int HTTP_METHOD_NOT_SUPORTED = 10021;
    public static final int IP_REQUEST_OUT_LIMIT = 100022;
    public static final int USER_REQUEST_OUT_LIMIT = 10023;
    public static final int USER_REQUEST_RATE_OUT_LIMIT = 10024;


    public static Map<Integer, String> sErrStrMap = new HashMap<Integer, String>();
    public static Map<Integer, Integer> sErrImgMap = new HashMap<Integer, Integer>();

    static {
        sErrStrMap.put(SYSTEM_EROR, "系统错误");
        sErrStrMap.put(SYSTEM_UNAVAILBALE, "服务暂停");
        sErrStrMap.put(REMOTE_SERVICE_ERROR, "远程服务错误");
        sErrStrMap.put(IP_LIMIT, "IP限制不能请求该资源");
        sErrStrMap.put(PERMISSION_DENIED, "该资源需要appkey拥有授权");
        sErrStrMap.put(SOURCE_PARAMTER, "缺少source (appkey) 参数");
        sErrStrMap.put(UNSIPPORT_MEDIA_TYPE, "不支持的MediaType(%s)");
        sErrStrMap.put(PARAM_ERROR, "参数错误，请参考API文档");
        sErrStrMap.put(SYSTEM_IS_BUSY, "任务过多，系统繁忙");
        sErrStrMap.put(JOB_EXPIRED, "任务超时");
        sErrStrMap.put(RPC_ERROR, "RPC错误");
        sErrStrMap.put(ILLEGAL_REQUEST, "非法请求");
        sErrStrMap.put(INVALID_WEIBO_USER, "不合法的微博用户");
        sErrStrMap.put(INSUFFICIENT_APP_PERMISSIONS, "应用的接口访问权限受限");
        sErrStrMap.put(MISS_REQUEIRED_PARAMETER, "缺失必选参数 (%s)，请参考API文档");
        sErrStrMap.put(PARAMETER_VALUE_INVALID, "参数值非法，需为 (%s)，实际为 (%s)，请参考API文档");
        sErrStrMap.put(REQUEST_LENGTH_LIMIT, "请求长度超过限制");
        sErrStrMap.put(API_NOT_FOUND, "接口不存在");
        sErrStrMap.put(HTTP_METHOD_NOT_SUPORTED, "请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式");
        sErrStrMap.put(IP_REQUEST_OUT_LIMIT, "请求次数超过新浪限制啦");
        sErrStrMap.put(USER_REQUEST_OUT_LIMIT, "请求次数超过新浪限制啦");
        sErrStrMap.put(USER_REQUEST_RATE_OUT_LIMIT, "请求次数超过新浪限制啦");

        sErrImgMap.put(USER_REQUEST_RATE_OUT_LIMIT, R.mipmap.d_dalian);
        sErrImgMap.put(USER_REQUEST_OUT_LIMIT, R.mipmap.d_dalian);
        sErrImgMap.put(IP_REQUEST_OUT_LIMIT, R.mipmap.d_dalian);
        sErrImgMap.put(SYSTEM_EROR, R.mipmap.d_bishi);
        sErrImgMap.put(SYSTEM_UNAVAILBALE, R.mipmap.d_bishi);
        sErrImgMap.put(IP_LIMIT, R.mipmap.d_shiwang);
        sErrImgMap.put(SYSTEM_IS_BUSY, R.mipmap.d_aoteman);
        sErrImgMap.put(JOB_EXPIRED, R.mipmap.d_aoteman);
        sErrImgMap.put(ILLEGAL_REQUEST, R.mipmap.d_bizui);
        sErrImgMap.put(INVALID_WEIBO_USER, R.mipmap.d_baibai);
    }


    public static void showError(ErrorInfo errorInfo) {
        ToastHelper.show(sErrStrMap.get(errorInfo.error_code), sErrImgMap.get(errorInfo.error_code));
    }

}
