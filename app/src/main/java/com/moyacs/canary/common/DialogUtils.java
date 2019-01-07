package com.moyacs.canary.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.blankj.utilcode.util.SPUtils;
import com.moyacs.canary.login.LoginActivity;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

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
    public static void login_please(String title, final Context context) {
        LemonHello.getInformationHello(title, "")
                .setContentFontSize(14)
                //取消图标
//                .setIconWidth(0)
                .addAction(new LemonHelloAction("取消", Color.GRAY, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                    }
                }))
                .addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        SPUtils.getInstance().clear();
                        context.startActivity(new Intent(context, LoginActivity.class));
                        lemonHelloView.hide();
                    }
                }))
                .show(context);
    }

}
