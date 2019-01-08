package com.moyacs.canary.network;

import android.text.TextUtils;

/**
 * Created by Shurrik on 2017/11/22.
 * <p>
 * 网络请求数据封装类
 */

public class HttpResult<T> {

    /**
     * result : 1
     * message : 请求成功
     * data :
     */

    private int code;
    private String message;
    private T dataObject;

    public T getDataObject() {
        return dataObject;
    }

    public void setDataObject(T data) {
        this.dataObject = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int result) {
        this.code = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return getCode() == 0;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", dataObject=" + dataObject +
                '}';
    }
}