package com.moyacs.canary.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.moyacs.canary.login.LoginActivity;
import com.moyacs.canary.util.SharePreferencesUtil;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;

/**
 * 作者：luoshen on 2018/4/20 0020 13:24
 * 邮箱：rsf411613593@gmail.com
 * 说明：封装的 三方 dialog
 */

public class DialogUtils {
    /**
     * 登录提示框
     *
     * @param title   显示的提示消息
     * @param context 山下文
     */
    public static void login_please(String title, final Context context, DialogMenuClickListener menuClickListener) {
        LemonHello.getInformationHello(title, "")
                .setContentFontSize(14)
                .addAction(new LemonHelloAction("取消", Color.GRAY, (helloView, helloInfo, helloAction) -> {
                    helloView.hide();
                    menuClickListener.onCancelListener();
                }))
                .addAction(new LemonHelloAction("确定", (lemonHelloView, lemonHelloInfo, lemonHelloAction) -> {
                    lemonHelloView.hide();
                    menuClickListener.onConfirmListener();
                }))
                .show(context);
    }

    public static void login_please(String title, final Context context) {
        LemonHello.getInformationHello(title, "")
                .setContentFontSize(14)
                //取消图标
//                .setIconWidth(0)
                .addAction(new LemonHelloAction("取消", Color.GRAY, (helloView, helloInfo, helloAction) -> {
                    helloView.hide();
                }))
                .addAction(new LemonHelloAction("确定", (lemonHelloView, lemonHelloInfo, lemonHelloAction) -> {
                    SharePreferencesUtil.getInstance().clean();
                    context.startActivity(new Intent(context, LoginActivity.class));
                    lemonHelloView.hide();
                }))
                .show(context);
    }

    public interface DialogMenuClickListener {
        void onCancelListener();

        void onConfirmListener();
    }

}
