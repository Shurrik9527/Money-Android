package com.moyacs.canary.network;

/**
 * Created by Administrator on 2017/12/12 0012.
 * <p>
 * 网络请求参数 字段
 */

public class HttpConstants {

    public static final String Base_URL = "http://47.91.164.170:8012/";
    //新版地址
    public static final String SERVER_HOST = "http://47.97.186.123/royal/";
    //融云
    public static final String SERVER_RONE_IM_HOST = "http://api-cn.ronghub.com/user/";
    //汇率
    public static final String SERVER_RATE_HOST = "http://api.k780.com";

//    public static final String SERVER_HOST = "http://www.wjhsh.cn/royal/";

    public static final String header_key = "Authorization";
    //    public static final String header_value = "Bearer"+" ";
    public static String header_value = "";

    public static final int default_connectTimeout = 10000;
    public static final int default_readTimeout = 10000;
    public static final int default_writeTimeout = 10000;
    //服务器返回字段 code 成功
    public static final int result_sucess = 0;

    /**
     * 网络请求异常
     */
    public static final String Exception_http = "hello?好像没网络啊！";
    public static final String Exception_http_connect = "网络连接超时，请检查您的网络状态！";
    public static final String Exception_server = "服务器开小差,请稍后重试！";
    public static final String Exception_json = "未能请求到数据，攻城狮正在修复!";
    public static final String Exception_io = "服务器异常";
    public static final String Exception_other = "未知错误";
    public static final String Exception_SSL = "证书错误";
    public static final String Exception_null = "数据为空";
    public static final String Exception_result = "服务器返回数据异常";
    /**
     * 网络请求参数
     */
    public static final String server = "server";//parent_id 分类接口参数
    public static final String cat_id = "cat_id";//cat_id
    public static final String type = "type";//商品类型 和 验证码类型
    public static final String sort = "sort";//商品排序方式
    public static final String goods_id = "goods_id";//商品id
    public static final String activity_id = "activity_id";//商品id

    public static final String code = "code";

    public static final String source = "source";
}
