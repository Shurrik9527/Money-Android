package com.moyacs.canary.main.me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import www.moyacs.com.myapplication.R;

public class SetNickNameActivity extends AppCompatActivity {

    @BindView(R.id.et_nickName)
    EditText etNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nick_name);
        ButterKnife.bind(this);
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
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showShort("昵称不能为空");
            return;
        }
        ServerManger.getInstance().getServer().updateNickName(nickName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult<String> stringServerResult) {
                        if (stringServerResult.isSuccess()) {
                            EventBus.getDefault().post(new EvenVo(EvenVo.EVENT_CODE_UPDATE_NICK_NAME));
                            ToastUtils.showShort("更新名字成功");
                            finish();
                        } else {
                            ToastUtils.showShort("服务器异常");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("服务器异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }




}
