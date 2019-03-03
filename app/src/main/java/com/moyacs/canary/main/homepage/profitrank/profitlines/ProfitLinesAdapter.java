package com.moyacs.canary.main.homepage.profitrank.profitlines;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.moyacs.canary.bean.ProfitLinesBean;
import www.moyacs.com.myapplication.R;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe  奖励额适配器
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitLinesAdapter extends BaseQuickAdapter<ProfitLinesBean, BaseViewHolder> {

    public ProfitLinesAdapter(Context context) {
        super(R.layout.view_profit_item_layout);
    }


    @Override
    protected void convert(BaseViewHolder helper, ProfitLinesBean item) {

    }


}
