package com.moyacs.canary.im;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.moyacs.canary.util.AppUtils;
import com.moyacs.canary.util.LogUtils;

import java.util.List;


import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 相关监听 事件集合类
 * @date 2019/3/21
 * @email 252774645@qq.com
 */
public class ChatAppContext implements
        RongIMClient.OnReceiveMessageListener,
        RongIM.ConversationBehaviorListener,
        RongIM.UserInfoProvider,
        RongIM.ConversationListBehaviorListener, RongIMClient.ConnectionStatusListener {


    private Context mContext;

    private static ChatAppContext mRongCloudInstance;


    public ChatAppContext(Context mContext) {
        this.mContext = mContext;
        RongYunUserInfoManager.init(mContext);
        initListener();

    }


    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {
            synchronized (ChatAppContext.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new ChatAppContext(context);
                }
            }
        }

    }


    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static ChatAppContext getInstance() {
        return mRongCloudInstance;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * init 后就能设置的监听
     */
    private void initListener() {
        setInputProvider();
    }


    private void setInputProvider() {
        RongIM.setOnReceiveMessageListener(this);     //全局消息监听
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setUserInfoProvider(this, true);      //用户信息提供者
        RongIM.setConversationListBehaviorListener(this);
        RongIM.setConnectionStatusListener(this);

    }

    @Override
    public boolean onReceived(Message message, int left) {

        // MessageNotificationManager.getInstance().notifyIfNeed(RongContext.getInstance(), message, left);
        //是否需要通知
        return notifyIfNeeed();
    }

    private boolean notifyIfNeeed() {

        //app在后台时
        boolean isInBackground = AppUtils.isAppBackground(mContext);
//        //app启动会话列表后台
//        boolean isInXlChat = AppUtils.getActivityManager()
//                .isExistActivity(ConversationListActivity.class);
        //app在聊天时
        boolean isExisConversation = !RongContext.getInstance().getCurrentConversationList().isEmpty();

        LogUtils.d("TAG" + "onReceived=> " + "isInBackground:" + isInBackground +  " ===== isExisConversation:" + isExisConversation);

        if (isInBackground || isExisConversation) {
            //默认通知流程
            LogUtils.d("TAG" + "onReceived=> " + "默认通知流程");
            return false;
        } else {
            //屏蔽通知
            LogUtils.d("TAG" + "onReceived=> " + "屏蔽通知");
            return true;
        }
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE || conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
            return false;
        }

        return true;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public UserInfo getUserInfo(String partyId) {
        RongYunUserInfoManager.getInstance().getUserInfo(partyId);
        return null;
    }


    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {

        return false;
    }


    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        //同时清理未读消息数
        buildMultiDialog(context, uiConversation);
        return true;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }


    private void buildMultiDialog(Context context, final UIConversation uiConversation) {
        String[] items = new String[2];
        if (uiConversation.isTop()) {
            items[0] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_cancel_top);
        } else {
            items[0] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top);
        }

        items[1] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_remove);
        OptionsPopupDialog.newInstance(context, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
            public void onOptionsItemClicked(int which) {
                if (which == 0) {
                    RongIM.getInstance().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean o) {
                            if (uiConversation.isTop()) {
                                Toast.makeText(RongContext.getInstance(), context.getString(io.rong.imkit.R.string.rc_conversation_list_popup_cancel_top), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RongContext.getInstance(), context.getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top), Toast.LENGTH_SHORT).show();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                } else if (which == 1) {
                    RongIM.getInstance().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), (RongIMClient.ResultCallback) null);
                    RongIM.getInstance().clearMessagesUnreadStatus(
                            uiConversation.getConversationType(), uiConversation.getConversationTargetId(), null);
                }

            }
        }).show();
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        switch (connectionStatus) {

            case CONNECTED://连接成功。

                break;
            case DISCONNECTED://断开连接。

                break;
            case CONNECTING://连接中。

                break;
            case NETWORK_UNAVAILABLE://网络不可用。

                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                //下线提示
//                PushSelector.logoutApp(new LogoutDataEven(  null
//                           , null,null));

                break;
        }
    }
}
