package com.moyacs.canary.moudle.recharge;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.util.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_rmb)
    TextView tvRmb;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.iv_03)
    ImageView iv03;
    private ImageView oldSelectImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        onViewClicked(findViewById(R.id.ll_bank));
        rvContent.setLayoutManager(new GridLayoutManager(this, 3, LinearLayout.VERTICAL, false));
        rvContent.addItemDecoration(new MyItemDecoration());
        rvContent.setAdapter(new RechargeAdapter());
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_break, R.id.ll_bank, R.id.ll_ali, R.id.ll_wei_chat, R.id.b_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_break:
                finish();
                break;
            case R.id.ll_bank:
                setSelectImage(iv01);
                break;
            case R.id.ll_ali:
                setSelectImage(iv02);
                break;
            case R.id.ll_wei_chat:
                setSelectImage(iv03);
                break;
            case R.id.b_recharge:
                break;
        }
    }

    private void setSelectImage(ImageView selectImage) {
        if (oldSelectImg != null) {
            oldSelectImg.setImageResource(R.mipmap.ic_un_select);
        }
        selectImage.setImageResource(R.mipmap.ic_select);
        oldSelectImg = selectImage;
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            int offset = ScreenUtil.dip2px(RechargeActivity.this, 16);
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
