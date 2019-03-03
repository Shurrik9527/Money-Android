package com.moyacs.canary.main.homepage.profitrank.profitrate;

import android.content.Context;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.moyacs.canary.bean.ProfitRateBean;
import www.moyacs.com.myapplication.R;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitRateAdapter extends BaseQuickAdapter<ProfitRateBean, BaseViewHolder> {

    public ProfitRateAdapter(Context context) {
        super(R.layout.view_profit_item_layout);
    }


    @Override
    protected void convert(BaseViewHolder helper, ProfitRateBean item) {

    }

}
