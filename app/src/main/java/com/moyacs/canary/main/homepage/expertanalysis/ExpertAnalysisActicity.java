package com.moyacs.canary.main.homepage.expertanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.HomeDealChanceBean;
import com.moyacs.canary.util.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ExpertAnalysisActicity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.top_title_tv)
    TextView topTitleTv;
    @BindView(R.id.expert_head_iv)
    ImageView expertHeadIv;
    @BindView(R.id.expert_name_tv)
    TextView expertNameTv;
    @BindView(R.id.expert_time_tv)
    TextView expertTimeTv;
    @BindView(R.id.tv_expert_tradeup)
    TextView tvExpertTradeup;
    @BindView(R.id.iv_expert_tradeup)
    ImageView ivExpertTradeup;
    @BindView(R.id.iv_expert_tradedown)
    ImageView ivExpertTradedown;
    @BindView(R.id.tv_expert_tradedown)
    TextView tvExpertTradedown;
    @BindView(R.id.base_analysis_tv)
    TextView baseAnalysisTv;
    @BindView(R.id.analysis_tv)
    TextView analysisTv;
    @BindView(R.id.techno_analysis_tv)
    TextView technoAnalysisTv;
    @BindView(R.id.techno_analysis_iv)
    ImageView technoAnalysisIv;
    @BindView(R.id.techno_tv)
    TextView technoTv;
    @BindView(R.id.bootom_tv)
    TextView bootomTv;
    @BindView(R.id.tv_expert_title)
    TextView tvExpertTitle;
    @BindView(R.id.tv_ecpert_themetitle)
    TextView tvEcpertThemetitle;


    private Intent mIntent = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_expertanalysis;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        mIntent = getIntent();
        if (mIntent != null) {
            HomeDealChanceBean bean = (HomeDealChanceBean) mIntent.getSerializableExtra("BEANS");
            if (bean != null) {

                //头像
                if (TextUtils.isEmpty(bean.getUserImg())) {
                    ImageLoader.getInstance().displayCircleImage(this, R.mipmap.avatar_default, expertHeadIv);
                } else {
                    ImageLoader.getInstance().displayCircleImage(this, bean.getUserImg(), expertHeadIv);
                }

                //姓名
                if (!TextUtils.isEmpty(bean.getUserName())) {
                    expertNameTv.setText(bean.getUserName());
                }
                //时间
                if (!TextUtils.isEmpty(bean.getCreateTime())) {
                    expertTimeTv.setText(bean.getCreateTime());
                }
                //主题
                if (!TextUtils.isEmpty(bean.getThemeText())) {
                    tvEcpertThemetitle.setText("【" + bean.getOperatingMode() + "】" + bean.getThemeText());
                }

                if (!TextUtils.isEmpty(bean.getTitle())) {
                    tvExpertTitle.setText("建议:"+bean.getTitle());
                }
                //基础面分析
                if (!TextUtils.isEmpty(bean.getFoundationAnalysis())) {
                    baseAnalysisTv.setText(bean.getTitle());
                }
                //技术面分析

                if (!TextUtils.isEmpty(bean.getTechnologicalAnalysis())) {
                    technoAnalysisTv.setText(bean.getTechnologicalAnalysis());
                }
                //线图
                if (!TextUtils.isEmpty(bean.getUserImg())) {
                    ImageLoader.getInstance().displayCircleImage(this, bean.getTechnologicalAnalysisImg(), technoAnalysisIv);
                }

            }
        }


        topTitleTv.setText(getString(R.string.experanalysis_top_tv));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }



}
