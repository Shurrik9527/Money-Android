/*
 * 乡邻小站
 *  Copyright (c) 2017 XiangLin,Inc.All Rights Reserved.
 */

package com.moyacs.canary.im;
import com.moyacs.canary.util.UserInfoManager;

import org.apache.commons.lang3.StringUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 消息发送监听
 * @date 2019/3/21
 * @email 252774645@qq.com
 */

public class MySendMessageListener implements RongIM.OnSendMessageListener {

    /**
     * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
     *
     * @param message 发送的消息实例。
     * @return 处理后的消息实例。
     */
    @Override
    public Message onSend(Message message) {

        MessageContent messageContent = message.getContent();
        if (messageContent != null) {

            //发送
            if (UserInfoManager.getInstance().getUserBean()!=null&& StringUtils.isEmpty(UserInfoManager.getInstance().getUserBean().getNickName())) {
                UserInfoManager.getInstance().getUserBean().setNickName(UserInfoManager.getInstance().getUserBean().getPartyId());
            }

            messageContent.setUserInfo(UserInfoManager.getInstance().converUserBeanRongyunUser(UserInfoManager.getInstance().getUserBean()));
        }

        return message;
    }

    /**
     * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message              消息实例。
     * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
     * @return true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {


        return false;
    }
}