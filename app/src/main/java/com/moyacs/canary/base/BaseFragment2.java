package com.moyacs.canary.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wang.avi.AVLoadingIndicatorView;

import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/1/5 0005 10:49
 * 邮箱：rsf411613593@gmail.com
 * 说明：Fragment 总基类
 * <p>
 * 1.统一布局中的 加载中动画，由子类决定显示或者隐藏
 * 2.当 Fragment 可见，并且视图创建完毕的时候，再加载数据，减少不必要的数据加载
 * 3.统一 activity 传递过来的 bundle 数据，由子类来处理
 * 4.懒加载
 */

public abstract class BaseFragment2 extends Fragment {

    private static final String TAG = "BaseFragment2";
    /**
     * fragment 是否对用户可见
     */
    protected boolean isVisibleToUser;
    /**
     * view 视图 是否创建完毕
     */
    protected boolean isViewCreated;
    /**
     * 根布局
     */
    private FrameLayout rootLayout;
    /**
     * 三方加载动画
     */
    private AVLoadingIndicatorView avLoading;
    private View view;
    private View inflaterView;
    private boolean isLoad = false; // 判断时候加载过数据

    /**
     * Fragment 是否可见
     * 因为 setUserVisibleHint 方法可能在 onViewCreated 方法之前调用，所以在这里也调用 lazyLoadData();
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        lazyLoadData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        initBundleData(bundle);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView:   onCreateView");
        view = inflater.inflate(R.layout.fragment_base, container, false);
        rootLayout = view.findViewById(R.id.root_layout);
        avLoading = view.findViewById(R.id.avi_loading);
        // 由子类决定填充的布局
        inflaterView = addChildInflaterView(inflater);
        if (inflaterView != null) {
            Log.i(TAG, "onCreateView:   addView");
            rootLayout.addView(inflaterView);
        }
        return view;
    }

    /**
     * 由子类决定填充的布局
     *
     * @param inflater
     * @return
     */
    protected abstract View addChildInflaterView(LayoutInflater inflater);

    /**
     * 处理由 activity 传递过来的 bundle 数据
     *
     * @param bundle
     */
    protected abstract void initBundleData(Bundle bundle);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onCreateView:    onViewCreated;");
        isViewCreated = true;
        lazyLoadData();

    }

    /**
     * 懒加载数据，只有当 fragment 可见 和 view 创建完毕的时候才加载数据
     */
    public void lazyLoadData() {
        Fragment parentFragment = getParentFragment();
        if (isViewCreated && isVisibleToUser && !isLoad) {
            loadData();
            isLoad = true;
        }
    }

    /**
     *
     */
    protected abstract void loadData();

    /**
     * 开启加载中动画
     */
    protected void startLoading() {
        if (avLoading == null) {
            return;
        }
        if (avLoading.isShown()) {
            return;
        }
        avLoading.smoothToShow();
    }

    /**
     * 关闭加载中动画
     */
    protected void stopLoading() {
        if (avLoading == null) {
            return;
        }
        if (avLoading.isShown()) {
            avLoading.smoothToHide();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        isVisibleToUser = false;
//        Log.i(TAG, "onDestroyView:   onDestroyView");
        if (inflaterView == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) inflaterView.getParent();
        if (parent != null) {
            Log.i(TAG, "onDestroyView:    parent.removeAllViews();");
            parent.removeAllViews();
        }
    }
}
