package com.moyacs.canary.main.market;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.main.market.adapter.MyOptionalAdapter;
import com.moyacs.canary.util.ScreenUtil;
import com.moyacs.canary.widget.UnderLineTextView;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * 自选页面
 */
public class OptionalActivity extends BaseActivity {
    @BindView(R.id.tv_myOptional)
    RecyclerView rvMyOptional;
    @BindView(R.id.tv_1)
    UnderLineTextView tv1;
    @BindView(R.id.tv_2)
    UnderLineTextView tv2;
    @BindView(R.id.tv_3)
    UnderLineTextView tv3;
    @BindView(R.id.rv_optional)
    RecyclerView rvOptional;

    private UnderLineTextView oldSelectView;
    private MyOptionalAdapter myOptionalAdapter;
    private MyOptionalAdapter optionalAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_optional;
    }

    @Override
    protected void initView() {
        onViewClicked(tv1);
        myOptionalAdapter = new MyOptionalAdapter(true);
        rvMyOptional.setLayoutManager(new GridLayoutManager(this, 3, LinearLayout.VERTICAL, false));
        rvMyOptional.addItemDecoration(new MyItemDecoration());
        rvMyOptional.setAdapter(myOptionalAdapter);

        optionalAdapter = new MyOptionalAdapter(false);
        rvOptional.setLayoutManager(new GridLayoutManager(this, 3, LinearLayout.VERTICAL, false));
        rvOptional.addItemDecoration(new MyItemDecoration());
        rvOptional.setAdapter(optionalAdapter);
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_break, R.id.tv_1, R.id.tv_2, R.id.tv_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_break:
                finish();
                break;
            case R.id.tv_1:
                setSelectTabView(tv1);
                break;
            case R.id.tv_2:
                setSelectTabView(tv2);
                break;
            case R.id.tv_3:
                setSelectTabView(tv3);
                break;
        }
    }

    private void setSelectTabView(UnderLineTextView selectTabView) {
        if (oldSelectView != null) {
            oldSelectView.setSelected(false);
        }
        selectTabView.setSelected(true);
        oldSelectView = selectTabView;
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            int offset = ScreenUtil.dip2px(OptionalActivity.this, 16);
            int itemContent = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            if ((pos + 1) <= itemContent) {
                outRect.top = offset;
            }
            if ((pos + 1) % itemContent == 0) {
                outRect.right = offset;
            }
            outRect.bottom = offset;
            outRect.left = offset;
        }
    }
}
