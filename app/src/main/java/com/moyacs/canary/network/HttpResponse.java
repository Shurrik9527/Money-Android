package com.moyacs.canary.network;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class HttpResponse <T>{

    private int msgCode;
    private String msg;
    private T data;

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
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

    public boolean isSuccess() {
        return getMsgCode()== 0;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "msgCode=" + msgCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
