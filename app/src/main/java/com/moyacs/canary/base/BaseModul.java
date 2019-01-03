package com.moyacs.canary.base;

/**
 * 作者：luoshen on 2018/3/5 0005 15:09
 * 邮箱：rsf411613593@gmail.com
 * 说明：modul 网络请求基类， modul 层进行网络请求（okhttp + retrofit）
 */

public interface BaseModul {
    /**
     * 解除所有订阅，防止内存泄漏
     */
    void unsubscribe();


}
