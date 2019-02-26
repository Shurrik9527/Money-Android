package com.moyacs.canary.base;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public interface BaseViews <T>{

    /**
     * 解除所有订阅，防止内存泄漏
     */
    void unsubscribe();

    /**
     * 显示加载框
     */
    void showLoadingDialog();

    /**
     * 取消加载框
     */
    void dismissLoadingDialog();

    //绑定Presenter
    void setPresenter(T presenter);

    //信息
    void showMessageTips(String msg);

}
