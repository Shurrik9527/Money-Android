package com.moyacs.canary.main.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.chad.library.adapter.base.BaseViewHolder;
import com.moyacs.canary.base.BaseDelegateAdapter;
import com.moyacs.canary.main.homepage.net.DealChanceDate;
import com.moyacs.canary.util.ForeignUtil;

import java.util.List;

import www.moyacs.com.myapplication.R;

public class DealChanceAdapter extends BaseDelegateAdapter {
    private List<DealChanceDate> chanceDateList;

    public DealChanceAdapter(Context context, LayoutHelper layoutHelper, int layoutId, int count, int viewTypeItem, List<DealChanceDate> chanceDateList) {
        super(context, layoutHelper, layoutId, count, viewTypeItem);
        this.chanceDateList = chanceDateList;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DealChanceDate dealChanceDate = chanceDateList.get(position);
        //品种名称
        TextView tv_name = holder.getView(R.id.tv_homeinformation_product);
        tv_name.setText(ForeignUtil.codeFormatCN(dealChanceDate.getName()));
        //趋势 看多
        TextView tv_trend = holder.getView(R.id.tv_homeinformation_abstract);
        tv_trend.setText(TextUtils.equals(dealChanceDate.getTrend(), "Sell") ? "卖" : "买");
        //年月日日期
        TextView tv_data = holder.getView(R.id.tv_data);
        tv_data.setText(dealChanceDate.getDate());
        //类型：专家解读
        TextView tv_type = holder.getView(R.id.tv_type);
        tv_type.setText(dealChanceDate.getType());
        //标题
        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(dealChanceDate.getTitle());
        //建议
        TextView tv_suggest = holder.getView(R.id.tv_suggest);
        tv_suggest.setText(dealChanceDate.getSuggest());
        //时分秒时间
        TextView tv_time = holder.getView(R.id.tv_time);
        tv_time.setText(dealChanceDate.getTime());
        //买涨比例
        TextView tv_tradeup = holder.getView(R.id.tv_homeinformation_tradeup);
        //买涨的数据 0-100
        int range = dealChanceDate.getRange();
        tv_tradeup.setText(range + "%用户买涨");
        //买涨 imageview
        ImageView iv_tradeup = holder.getView(R.id.iv_homeinformation_tradeup);
        LinearLayout.LayoutParams layoutParams_old =
                (LinearLayout.LayoutParams) iv_tradeup.getLayoutParams();
        LinearLayout.LayoutParams layoutParams_new =
                new LinearLayout.LayoutParams(0, layoutParams_old.height, range);
        iv_tradeup.setLayoutParams(layoutParams_new);
        //买跌比例
        TextView tv_tradeDown = holder.getView(R.id.tv_homeinformation_tradedown);
        tv_tradeDown.setText(100 - range + "%用户买跌");
        //买跌 imageView
        ImageView iv_tradeDown = holder.getView(R.id.iv_homeinformation_tradedown);
        LinearLayout.LayoutParams layoutParams_old1 =
                (LinearLayout.LayoutParams) iv_tradeDown.getLayoutParams();
        LinearLayout.LayoutParams layoutParams_new1 =
                new LinearLayout.LayoutParams(0, layoutParams_old1.height, 100 - range);
        iv_tradeDown.setLayoutParams(layoutParams_new1);
    }
}
