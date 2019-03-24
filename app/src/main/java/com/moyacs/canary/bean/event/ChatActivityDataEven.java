package com.moyacs.canary.bean.event;

import android.net.Uri;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/23
 * @email 252774645@qq.com
 */
public class ChatActivityDataEven {
    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    public ChatActivityDataEven(Uri uri) {
        this.uri = uri;
    }
}
