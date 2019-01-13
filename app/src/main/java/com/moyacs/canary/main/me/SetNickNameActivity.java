package com.moyacs.canary.main.me;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

public class SetNickNameActivity extends BaseActivity {

    @BindView(R.id.et_nickName)
    EditText etNickName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_nick_name;
    }

    @Override
    protected void initView() {
        String nickName = SharePreferencesUtil.getInstance().getNickName();
        if (!TextUtils.isEmpty(nickName)) {
            etNickName.setText(SharePreferencesUtil.getInstance().getNickName());
            etNickName.setSelection(etNickName.getText().length());
        }
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                uploadNickName(etNickName.getText().toString());
                break;
        }
    }

    private void uploadNickName(String nickName) {
        showLoadingDialog();
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showShort("昵称不能为空");
            return;
        }
        addSubscribe(ServerManger.getInstance().getServer().updateNickName(nickName)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        EventBus.getDefault().post(new EvenVo(EvenVo.EVENT_CODE_UPDATE_NICK_NAME));
                        ToastUtils.showShort("更新名字成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showShort("服务器异常");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissLoadingDialog();
                    }
                }));
    }


}
