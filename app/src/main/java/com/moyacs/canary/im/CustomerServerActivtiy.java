package com.moyacs.canary.im;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.CSCustomServiceInfo;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 在线客服
 * @date 2019/3/3
 * @email 252774645@qq.com
 */
public class CustomerServerActivtiy extends FragmentActivity {

    private static final String RONG_CUSTOMER_SERVER_ID="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);
        ConversationFragment fragment = new ConversationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

//        //启动客服服务
//        //首先需要构造使用客服者的用户信息
//        try {
//            CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
//            CSCustomServiceInfo csInfo = csBuilder.nickName("融云").build();
//            RongIM.getInstance().startCustomerServiceChat(this,RONG_CUSTOMER_SERVER_ID, "在线客服",csInfo);
//        }catch (Exception e){
//
//        }
    }


    @Override
    public void onBackPressed() {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.rong_content);
        if(!fragment.onBackPressed()) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        onBackPressed();
        return super.onKeyDown(keyCode, event);
    }

}
