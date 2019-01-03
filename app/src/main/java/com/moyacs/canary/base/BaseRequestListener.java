package com.moyacs.canary.base;

/**
 * 作者：luoshen on 2018/3/5 0005 15:31
 * 邮箱：rsf411613593@gmail.com
 * 说明：M 层 请求结果 回调给 P 层 的 接口回调基类
 */

public interface BaseRequestListener {

    /**
     * 请求数据之前的初始化操作,如：显示 dialog,也能取消订阅
     */
    void beforeRequest();


    /**
     * 请求结束，取消 dialog 等
     */
    void afterRequest();
}
