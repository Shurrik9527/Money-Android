package com.moyacs.canary.main.me;

public class EvenVo<T> {
    public static final int EVENT_CODE_UPDATE_NICK_NAME = 0X11;
    public static final int UPDATE_MY_CHOICE = 0X12;
    public static final int SOCKET_QUOTATION=0X13;
    private int code;
    private  T t;
    public EvenVo(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public T getT() {
        return t;
    }

    public EvenVo<T> setT(T t) {
        this.t = t;
        return this;
    }
}
