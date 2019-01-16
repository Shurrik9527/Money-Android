package com.moyacs.canary.network;

import android.text.TextUtils;

public class ServerResult<T> {
    private String msgCode;
    private String msg;
    private T data;
    private String msgTime;

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public boolean isSuccess() {
        return 0 == ((getMsgCode() == null || TextUtils.isEmpty(getMsgCode())) ? -1 : Integer.parseInt(getMsgCode()));
    }
}
