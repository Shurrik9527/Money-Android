package com.moyacs.canary.im;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.just.library.LogUtils;
import com.moyacs.canary.base.BaseActivity2;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 在线客服
 */
public class KefuActivity extends BaseActivity2 {
    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_content)
    EditText et_content;

    private Unbinder unbinder;

    private ChatAdapter  mAdapter;
    private List<IMMessage> data;


    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_msg).setVisible(false);
    }

    @Override
    protected void initIntentData(Intent intent) {
        Observer<List<IMMessage>> incomingMessageObserver =
                new Observer<List<IMMessage>>() {
                    @Override
                    public void onEvent(List<IMMessage> messages) {
                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                        for(IMMessage message : messages){
                            data.add(message);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                };
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_kefu, null, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        data = new ArrayList<IMMessage>();
        mAdapter = new ChatAdapter(data);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }


    @Override
    protected boolean isshowToolbar() {
        return true;
    }


    @Override
    protected String setToolbarTitle(String title) {
        return "在线客服";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick(R.id.b_send)
    public void onViewClicked() {
        // 该帐号为示例，请先注册
        String account = "kefu01";
        //String account = "kehu5";
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        Editable text =  et_content.getText();
        // 创建一个文本消息
        IMMessage textMessage = MessageBuilder.createTextMessage(account, sessionType, text.toString());
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
        // 清空
        text.clear();
        data.add(textMessage);
        mAdapter.notifyDataSetChanged();
    }
}
