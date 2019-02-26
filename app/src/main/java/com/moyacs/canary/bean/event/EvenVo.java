package com.moyacs.canary.bean.event;

public class EvenVo<T> {
    public static final int EVENT_CODE_UPDATE_NICK_NAME = 0X11; //更新我的昵称
    public static final int UPDATE_MY_CHOICE = 0X12; //更新我的新信息
    public static final int SOCKET_QUOTATION=0X13; //socket数据刷新
    public static final int WATCH_CHI_CHAN = 0X14; //查看持仓
    public static final int CHANGE_ORDER_SUCCESS=0X15; //交易成功
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
