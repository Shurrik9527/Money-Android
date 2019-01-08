package com.moyacs.canary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.moyacs.canary.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private boolean isRegisterEventBus;
    private CompositeDisposable disposables;
    private Unbinder bind;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        initView();
        intListener();
        initData();
    }

    protected void addSubscribe(Disposable disposable) {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        disposables.add(disposable);
    }

    @Override
    protected void onDestroy() {
        unRegisterEventBus();
        unsubscribe();
        bind.unbind();
        if (disposables != null) {
            disposables.clear();
        }
        if (loadingDialog != null) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
        super.onDestroy();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void intListener();

    protected abstract void initData();

    protected void showMsg(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected void registerEventBus() {
        isRegisterEventBus = true;
        EventBus.getDefault().register(this);
    }

    private void unRegisterEventBus() {
        if (isRegisterEventBus) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void unsubscribe() {

    }
}
