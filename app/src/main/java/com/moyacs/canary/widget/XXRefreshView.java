package com.moyacs.canary.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.andview.refreshview.XRefreshView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/28
 * @email 252774645@qq.com
 */
public class XXRefreshView extends XRefreshView {

    public XXRefreshView(Context context) {
        super(context);
        init(context);
    }

    public XXRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化 刷新布局
     * @param context
     */
    private void init(Context context) {
        setAutoRefresh(false);
        setPullLoadEnable(true);
        setAutoLoadMore(false);
        setMoveForHorizontal(true);
        setPullRefreshEnable(true);
        enableReleaseToLoadMore(false);
        enableRecyclerViewPullUp(true);
        enablePullUpWhenLoadCompleted(true);

    }

}
