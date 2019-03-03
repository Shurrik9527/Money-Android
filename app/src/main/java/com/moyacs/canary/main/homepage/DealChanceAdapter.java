package com.moyacs.canary.main.homepage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.android.vlayout.LayoutHelper;
import com.chad.library.adapter.base.BaseViewHolder;
import com.moyacs.canary.base.BaseDelegateAdapter;
import com.moyacs.canary.bean.HomeDealChanceBean;
import com.moyacs.canary.util.ForeignUtil;
import com.moyacs.canary.util.ViewListenerAbs;

import java.util.List;

import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 首页交易机会适配器
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class DealChanceAdapter extends BaseDelegateAdapter {
    private List<HomeDealChanceBean> chanceDateList;
    private ViewListenerAbs.ItemClickListener itemClickListener;
    public DealChanceAdapter(Context context, LayoutHelper layoutHelper, int layoutId, int count, int viewTypeItem, List<HomeDealChanceBean> chanceDateList) {
        super(context, layoutHelper, layoutId, count, viewTypeItem);
        this.chanceDateList = chanceDateList;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        HomeDealChanceBean dealChanceDate = chanceDateList.get(position);
        //品种名称
        TextView tv_name = holder.getView(R.id.tv_homeinformation_product);
        tv_name.setText(ForeignUtil.codeFormatCN(dealChanceDate.getSymbolName()));
        //趋势 看多
        TextView tv_trend = holder.getView(R.id.tv_homeinformation_abstract);
       // tv_trend.setText(TextUtils.equals(dealChanceDate.getTrend(), "Sell") ? "卖" : "买");
        //年月日日期
        TextView tv_data = holder.getView(R.id.tv_data);
        tv_data.setText(dealChanceDate.getCreateTime());
        //类型：专家解读
        TextView tv_type = holder.getView(R.id.tv_type);
        tv_type.setText(dealChanceDate.getOperatingMode());
        //标题
        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(dealChanceDate.getTitle());
        //建议
        TextView tv_suggest = holder.getView(R.id.tv_suggest);
        tv_suggest.setText(dealChanceDate.getFoundationAnalysis());
        //时分秒时间
        TextView tv_time = holder.getView(R.id.tv_time);
        tv_time.setText(dealChanceDate.getCreateTime());
        //买涨比例
        TextView tv_tradeup = holder.getView(R.id.tv_homeinformation_tradeup);
        //买涨的数据 0-100
        int range = dealChanceDate.getRisePercentage();
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


        LinearLayout mainRl =holder.getView(R.id.home_deal_chance_rl);
        mainRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener(v, position);
                }
            }
        });


    }

    public void setItemClickListener(ViewListenerAbs.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public List<HomeDealChanceBean> getData(){
        return chanceDateList;
    }

}
