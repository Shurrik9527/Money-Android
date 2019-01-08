package com.moyacs.canary.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.moyacs.canary.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment implements BaseView {
    private boolean isRegisterEventBus;
    protected Activity mActivity;
    private CompositeDisposable disposables;
    Unbinder unbinder;
    private LoadingDialog loadingDialog;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        mView = LayoutInflater.from(mActivity).inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mView);
        initView();
        intListener();
        initData();
        return mView;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        unRegisterEventBus();
        if (disposables != null) {
            disposables.clear();
        }
        if (loadingDialog != null) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
        unsubscribe();
        super.onDestroyView();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void intListener();

    protected abstract void initData();

    protected void showMag(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mActivity);
        }
        loadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected void addSubscribe(Disposable disposable) {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        disposables.add(disposable);
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
