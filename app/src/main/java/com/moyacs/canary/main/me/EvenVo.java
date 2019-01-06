package com.moyacs.canary.main.me;

public class EvenVo {
    public static final int EVENT_CODE_UPDATE_NICK_NAME = 0X11;
    private int code;

    public EvenVo(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
