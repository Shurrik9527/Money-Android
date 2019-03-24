
package com.moyacs.canary.im;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moyacs.canary.MyApplication;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.event.ChatActivityDataEven;
import com.moyacs.canary.login.LoginActivity;
import com.moyacs.canary.main.MainActivity;
import com.moyacs.canary.moudle.guide.WelcomeActivity;
import com.moyacs.canary.util.AppUtils;
import com.moyacs.canary.util.KeyboardUtils;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.widget.DialogManager;
import com.moyacs.canary.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.typingmessage.TypingStatus;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 聊天UI
 * @date 2019/3/21
 * @email 252774645@qq.com
 */
public class ConversationActivity extends BaseActivity {

    private static final  String TAG =ConversationActivity.class.getName();
    MySendMessageListener mySendMessageListener;

    /**
     * 对方id
     */
    private String mTargetId;
    private Handler mHandler;
    private final String TextTypingTitle = "对方正在输入...";
    private final String VoiceTypingTitle = "对方正在讲话...";

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;

    private TextView mTitle;
    /**
     * title
     */
    private String title = "客服";
    private boolean isFromPush;
    private Conversation.ConversationType mConversationType;
    private ConversationFragment fragment;


    private void requestChatPermissions() {

        // android 6.0 以上版本，监听SDK权限请求，弹出对应请求框。
        if (Build.VERSION.SDK_INT >= 23) {
            RongIM.getInstance().setRequestPermissionListener(new MyRequestPermissionsListener(this));
        }
    }

    private void setActionBarTitle(Conversation.ConversationType mConversationType, String mTargetId) {

        if (mConversationType == null)
            return;

        if (mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
            setPrivateActionBar(mTargetId);
        } else {
            setTitle(R.string.de_actionbar_sub_defult);
        }
    }

    private void setTitle(String title){
        if(mTitle!=null){
            mTitle.setText(title);
        }
    }
    /**
     * 设置私聊界面 ActionBar
     */
    private void setPrivateActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            if (title.equals("null")) {
                if (!TextUtils.isEmpty(targetId)) {
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
                    if (userInfo != null) {
                        setTitle(userInfo.getName());
                    }
                }
            } else {
                setTitle(title);
            }

        } else {
            setTitle(targetId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SendMessageListener 如果在 Activity 里设置，需要在 Activity 销毁时，将监听设置为 null，防止内存泄露。
        mySendMessageListener = null;
        RongIM.getInstance().setSendMessageListener(null);
        RongIMClient.setTypingStatusListener(null);
        if (Build.VERSION.SDK_INT >= 23) {
            RongIM.getInstance().setRequestPermissionListener(null);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conversation;
    }

    @Override
    protected void initView() {
        mTitle =findViewById(R.id.title_tv);

        ImageView mImageview =findViewById(R.id.iv_back);
        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent == null || intent.getData() == null)
            return;

        mTargetId = intent.getData().getQueryParameter("targetId");
        //10000 为 Demo Server 加好友的 id，若 targetId 为 10000，则为加好友消息，默认跳转到 NewFriendListActivity
        // Demo 逻辑
        if (mTargetId != null && mTargetId.equals("10000")) {
            //startActivity(new Intent(ConversationActivity.this, ContactsActivity.class));
            return;
        }
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.getDefault()));

//        title = intent.getData().getQueryParameter("title");
//        if ("null".equals(title) || TextUtils.isEmpty(title)) {
//            title = "客服";
//        }
        //setTitle(title);
        mTitle.setText("客服");
        isPushMessage(intent);

        requestChatPermissions();
        mySendMessageListener = new MySendMessageListener();
        RongIM.getInstance().setSendMessageListener(mySendMessageListener);


        mHandler = new Handler(new MyCallback(ConversationActivity.this));

        RongIMClient.setTypingStatusListener(new MyTypingStatusListener(this));

    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {

    }

    private void isPushMessage(Intent intent) {

        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                //只有收到系统消息和不落地 push 消息的时候，pushId 不为 null。而且这两种消息只能通过 server 来发送，客户端发送不了。
                //RongIM.getInstance().getRongIMClient().recordNotificationEvent(id);
                new LoadingDialog(ConversationActivity.this);
                DialogManager.showProgressDialog(ConversationActivity.this);

                isFromPush = true;
                enterActivity();
            } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                DialogManager.showProgressDialog(ConversationActivity.this);
                enterActivity();
            } else {
                enterFragment(mConversationType, mTargetId);
            }

        } else {
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                DialogManager.showProgressDialog(ConversationActivity.this);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enterActivity();
                    }
                }, 300);
            } else {
                enterFragment(mConversationType, mTargetId);
            }
        }
    }

    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {


        fragment = new ConversationFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.chat_content, fragment);
        transaction.commitAllowingStateLoss();

    }


    /**
     * 收到 push 消息后，选择进入哪个 Activity
     * 如果程序缓存未被清理，进入 MainActivity
     * 程序缓存被清理，进入 LoginActivity，重新获取token
     * <p>
     * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationActivity 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。
     * 以跳到 MainActivity 为例：
     * 在 ConversationActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity 激活了，当你读完收到的消息点击 返回键 时，程序会退到
     * MainActivity 页面，而不是直接退回到 桌面。
     */
    private void enterActivity() {


        long id = SharePreferencesUtil.getInstance().getPartyId();

        if (id == -1) {
            Log.e(TAG,"ConversationActivity push" + "push2");
            startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
        } else {
            Log.e(TAG,"ConversationActivity push" + "push3");
            if (!TextUtils.isEmpty(MyApplication.ryToken)) {
                reconnect(MyApplication.ryToken);
            } else {

                boolean isExist = AppUtils.getActivityManager().isExistActivity(MainActivity.class);
                if (!isExist) {
                    Intent mIntent = new Intent(MyApplication.instance, WelcomeActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    MyApplication.instance.startActivity(mIntent);
                    EventBus.getDefault().postSticky(new ChatActivityDataEven(getIntent().getData()));
                } else {
                    DialogManager.hideProgressDialog();
                    ToastUtils.showShort("连接聊天服务器失败");
                }

            }

        }
    }


    private void reconnect(String token) {


        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                DialogManager.hideProgressDialog();

                Log.d(TAG,"---onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String s) {
                Log.d(TAG,"---onSuccess--" + s);
                Log.d(TAG,"ConversationActivity push" + "push4");

                DialogManager.hideProgressDialog();

                enterFragment(mConversationType, mTargetId);

            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                Log.d(TAG,"---onError--" + e);
                DialogManager.hideProgressDialog();

                enterFragment(mConversationType, mTargetId);
            }
        });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KeyboardUtils.hideSoftInput(this);
    }


    private static class MyCallback implements Handler.Callback {


        WeakReference<ConversationActivity> mActivity;

        public MyCallback(ConversationActivity activity) {
            mActivity = new WeakReference<ConversationActivity>(activity);
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (mActivity.get() == null) return true;
            switch (msg.what) {
                case SET_TEXT_TYPING_TITLE:
                    mActivity.get().setTitle(mActivity.get().TextTypingTitle);
                    break;
                case SET_VOICE_TYPING_TITLE:
                    mActivity.get().setTitle(mActivity.get().VoiceTypingTitle);
                    break;
                case SET_TARGET_ID_TITLE:
                    mActivity.get().setActionBarTitle(mActivity.get().mConversationType, mActivity.get().mTargetId);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private static class MyRequestPermissionsListener implements RongIM.RequestPermissionsListener {

        WeakReference<ConversationActivity> mActivity;

        public MyRequestPermissionsListener(ConversationActivity activity) {
            mActivity = new WeakReference<ConversationActivity>(activity);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onPermissionRequest(String[] permissions, final int requestCode) {

            if (mActivity.get() == null) return;

            for (final String permission : permissions) {
                if (mActivity.get().shouldShowRequestPermissionRationale(permission)) {
                    mActivity.get().requestPermissions(new String[]{permission}, requestCode);
                } else {
                    int isPermissionGranted = mActivity.get().checkSelfPermission(permission);
                    if (isPermissionGranted != PackageManager.PERMISSION_GRANTED) {
                        new AlertDialog.Builder(mActivity.get())
                                .setMessage("你需要在设置里打开以下权限:" + permission)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mActivity.get().requestPermissions(new String[]{permission}, requestCode);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create().show();
                    }
                    return;
                }
            }
        }
    }

    private static class MyTypingStatusListener implements RongIMClient.TypingStatusListener {

        WeakReference<ConversationActivity> mActivity;

        public MyTypingStatusListener(ConversationActivity activity) {
            mActivity = new WeakReference<ConversationActivity>(activity);
        }

        @Override
        public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
            if (mActivity.get() == null) return;

            //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
            if (type.equals(mActivity.get().mConversationType) && targetId.equals(mActivity.get().mTargetId)) {
                int count = typingStatusSet.size();
                //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                if (count > 0) {
                    Iterator iterator = typingStatusSet.iterator();
                    TypingStatus status = (TypingStatus) iterator.next();
                    String objectName = status.getTypingContentType();

                    MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                    MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                    //匹配对方正在输入的是文本消息还是语音消息
                    if (objectName.equals(textTag.value())) {
                        mActivity.get().mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                    } else if (objectName.equals(voiceTag.value())) {
                        mActivity.get().mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                    }
                } else {//当前会话没有用户正在输入，标题栏仍显示原来标题
                    mActivity.get().mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                }
            }
        }
    }
}
