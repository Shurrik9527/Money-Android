package com.moyacs.canary.network;

public class ServerResult<T> {
    private String msgCode;
    private String msg;
    private T data;
    private String msgTime;

    public int getMsgCode() {
        return msgCode == null ? -1 : Integer.parseInt(msgCode);
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
        return 0 == getMsgCode();
    }
}
