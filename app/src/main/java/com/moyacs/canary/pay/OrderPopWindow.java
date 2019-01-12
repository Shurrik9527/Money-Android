package com.moyacs.canary.pay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moyacs.canary.common.RSAKeyManger;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.ForeignUtil;
import com.moyacs.canary.util.LogUtils;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.widget.UnderLineTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/4/4 0004 16:22
 * 邮箱：rsf411613593@gmail.com
 * 说明：下单页面的 popwindow 因为多个地方用到所以需要抽取出来
 */

public class OrderPopWindow implements View.OnClickListener {
    /**
     * popwindow 关闭按钮
     */
    ImageView gobackView;
    /**
     * 品种名称
     */
    UnderLineTextView tvYin;
    /**
     * 品种实时价格
     */
    TextView tvPrice01;
    TextView tvRate;
    TextView tvRateChange;
    TextView textGiftname;
    RelativeLayout tabView;
    TextView textUpView;
    /**
     * 买涨比例
     */
    TextView textByuprate;
    RelativeLayout upView;
    TextView textDownView;
    /**
     * 买跌比例
     */
    TextView textBydownrate;
    RelativeLayout downView;
    /**
     * 多少钱一手 ，价格一
     */
    TextView tvMongey01;
    /**
     * 多少钱一手 ，价格二
     */
    TextView tvMongey02;
    /**
     * 多少钱一手 ，价格三
     */
    TextView tvMongey03;
    /**
     * 1手
     */
    TextView tvSize01;
    /**
     * 5 手
     */
    TextView tvSize02;
    /**
     * 10手
     */
    TextView tvSize03;
    /**
     * 其他
     */
    TextView tvSizeOther;
    /**
     * 建仓第一行布局，1手 5手 10手 其他
     */
    LinearLayout quickSizeView;
    /**
     * 点击其他之后显示出来的建仓第二行布局
     */
    LinearLayout sizeOther01;
    /**
     * 点击其他之后显示出来的建仓第三行布局
     */
    LinearLayout sizeOther02;
    /**
     * 点击其他之后显示的建仓总布局
     */
    LinearLayout sizeOtherView;
    /**
     * 每增长1个点，能收益5元
     */
    TextView textRatetips;
    /**
     * 是否选择余额的标志
     */
    ImageView checkUsecasher;
    /**
     * 具体的余额
     */
    TextView tvBalance;
    /**
     * 余额总布局
     */
    LinearLayout lineCheckUsecasher;
    /**
     * 充值按钮
     */
    TextView tvCashin;
    /**
     * 是否选择代金券的标志
     */
    ImageView checkUsequan;
    /**
     * 代金券数量
     */
    TextView tvQuanCount;
    /**
     * 代金券总布局
     */
    LinearLayout lineCheckUsequan;
    /**
     * 止损点数
     */
    TextView tvZhisunValue;
    /**
     * 止损价格
     */
    TextView tvZhisunMoney;
    /**
     * 止损左侧 减号按钮
     */
    Button btnZhisunLess;
    /**
     * 止损 seekbar
     */
    SeekBar seekBarZhisun;
    /**
     * seekbar右侧的最大值
     */
    TextView tvMaxZhisun;
    /**
     * 止损右侧 加号 btn
     */
    Button btnZhisunAdd;
    /**
     * 止盈点数
     */
    TextView tvZhiyingValue;
    /**
     * 止盈价格
     */
    TextView tvZhiyingMoney;
    /**
     * 止盈 左侧 点击按钮
     */
    Button btnZhiyingLess;
    /**
     * 止盈 seekbar
     */
    SeekBar seekBarZhiying;
    /**
     * seekbar 右侧最大值
     */
    TextView tvMaxZhiying;
    /**
     * 止盈 右侧 加号 按钮
     */
    Button btnZhiyingAdd;
    /**
     * 总价
     */
    TextView tvTotalMoney;
    /**
     * 总价单位
     */
    TextView tvUnit;
    /**
     * 手续费
     */
    TextView tvFee;
    /**
     * 手续费提醒信息
     */
    TextView textGiftremark;
    /**
     * 下单按钮
     */
    Button btnSubmit;
    private TextView tvOverNight;

    private Activity baseActivity2;

    /**
     * 建仓 和 类型 点击切换状态时候的 第三方变量，记录当前选中的 view
     */
    private View leiXingView, jianCangView;

    private Unbinder unbinder;
    /**
     * 是否获取到新价格
     */
    private boolean isGetNewPrice = false;
    /**
     * 是否是买涨
     */
    private boolean isUp;
    /**
     * 下单页面
     */
    private PopupWindow popupWindow_order;

    //品种中文名称
    private String symbol_cn;
    //品种和买入价颜色
    private int textColor;
    //买入价
    private String price_buy;
    private String symbolCode; //产品编码
    private TradeVo.Trade trade;

    public OrderPopWindow(String price_buy, String symbol_cn, int textColor, String symbolCode, TradeVo.Trade trade, Activity baseActivity2) {
        this.price_buy = price_buy;
        this.symbol_cn = symbol_cn;
        this.textColor = textColor;
        this.symbolCode = symbolCode;
        this.baseActivity2 = baseActivity2;
        this.trade = trade;
        //pop 布局
        View rootView = View.inflate(baseActivity2, R.layout.popwindow_create_order, null);
        unbinder = ButterKnife.bind(baseActivity2, rootView);
        popupWindow_order = new PopupWindow(rootView, LinearLayout.LayoutParams.MATCH_PARENT,
                baseActivity2.getResources().getDisplayMetrics().heightPixels * 4 / 5, true);
        //设置背景色
        popupWindow_order.setBackgroundDrawable(new ColorDrawable(0));
        //动画效果
        popupWindow_order.setAnimationStyle(R.style.popwin_anim_style);
        //点击空白位置，popupwindow消失的事件监听，这时候让背景恢复正常
        popupWindow_order.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        initViews(rootView);
        initData();
    }

    private void initData() {
        //品种名称
        tvYin.setText(symbol_cn);
        tvYin.setTextColor(textColor);
        //品种价格
        tvPrice01.setText(price_buy);
        tvPrice01.setTextColor(textColor);
        tvMongey01.setText(trade.getUnit_price_one() + "元/手");
        tvMongey02.setText(trade.getUnit_price_two() + "元/手");
        tvMongey03.setText(trade.getUnit_price_three() + "元/手");
    }

    /**
     * 显示建仓的  popwindow
     *
     * @param view
     * @param isUp 是否是买涨
     */
    public void showOrderPopwindow(View view, boolean isUp) {
        this.isUp = isUp;
        //初始化 view 相关数据 和 参数
        initOrderPopwindowViews(isUp);
        //设置背景半透明
        backgroundAlpha(0.6f);
        //位置
        popupWindow_order.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        onClick(tvMongey01);
        onClick(tvSize01);
    }

    /**
     * 初始化 view
     *
     * @param popView
     */
    private void initViews(View popView) {
        tvYin = popView.findViewById(R.id.tv_yin);
        gobackView = popView.findViewById(R.id.gobackView);
        tvPrice01 = popView.findViewById(R.id.tv_price01);
        tvRate = popView.findViewById(R.id.tv_rate);
        tvRateChange = popView.findViewById(R.id.tv_rateChange);
        textGiftname = popView.findViewById(R.id.text_giftname);
        tabView = popView.findViewById(R.id.tabView);
        textUpView = popView.findViewById(R.id.textUpView);
        textByuprate = popView.findViewById(R.id.text_byuprate);
        upView = popView.findViewById(R.id.upView);
        textDownView = popView.findViewById(R.id.textDownView);
        textBydownrate = popView.findViewById(R.id.text_bydownrate);
        downView = popView.findViewById(R.id.downView);
        tvMongey01 = popView.findViewById(R.id.tv_mongey01);
        tvMongey02 = popView.findViewById(R.id.tv_mongey02);
        tvMongey03 = popView.findViewById(R.id.tv_mongey03);
        tvSize01 = popView.findViewById(R.id.tv_size01);
        tvSize02 = popView.findViewById(R.id.tv_size02);
        tvSize03 = popView.findViewById(R.id.tv_size03);
        tvSizeOther = popView.findViewById(R.id.tv_sizeOther);
        quickSizeView = popView.findViewById(R.id.quickSizeView);
        sizeOther01 = popView.findViewById(R.id.sizeOther01);
        sizeOther02 = popView.findViewById(R.id.sizeOther02);
        sizeOtherView = popView.findViewById(R.id.sizeOtherView);
        textRatetips = popView.findViewById(R.id.text_ratetips);
        checkUsecasher = popView.findViewById(R.id.check_usecasher);
        tvBalance = popView.findViewById(R.id.tv_balance);
        lineCheckUsecasher = popView.findViewById(R.id.line_check_usecasher);
        tvCashin = popView.findViewById(R.id.tv_cashin);
        checkUsequan = popView.findViewById(R.id.check_usequan);
        tvQuanCount = popView.findViewById(R.id.tv_quanCount);
        lineCheckUsequan = popView.findViewById(R.id.line_check_usequan);
        tvZhisunValue = popView.findViewById(R.id.tv_zhisunValue);
        tvZhisunMoney = popView.findViewById(R.id.tv_zhisunMoney);
        btnZhisunLess = popView.findViewById(R.id.btnZhisunLess);
        seekBarZhisun = popView.findViewById(R.id.seekBarZhisun);
        tvMaxZhisun = popView.findViewById(R.id.tv_maxZhisun);
        btnZhisunAdd = popView.findViewById(R.id.btnZhisunAdd);
        tvZhiyingValue = popView.findViewById(R.id.tv_zhiyingValue);
        tvZhiyingMoney = popView.findViewById(R.id.tv_zhiyingMoney);
        btnZhiyingLess = popView.findViewById(R.id.btnZhiyingLess);
        seekBarZhiying = popView.findViewById(R.id.seekBarZhiying);
        tvMaxZhiying = popView.findViewById(R.id.tv_maxZhiying);
        btnZhiyingAdd = popView.findViewById(R.id.btnZhiyingAdd);
        tvTotalMoney = popView.findViewById(R.id.tv_totalMoney);
        tvUnit = popView.findViewById(R.id.tv_unit);
        tvFee = popView.findViewById(R.id.tv_fee);
        textGiftremark = popView.findViewById(R.id.text_giftremark);
        btnSubmit = popView.findViewById(R.id.btn_submit);
        tvOverNight = popView.findViewById(R.id.text_overNight);

        gobackView.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        lineCheckUsequan.setOnClickListener(this);
        btnZhisunLess.setOnClickListener(this);
        btnZhisunAdd.setOnClickListener(this);
        btnZhiyingLess.setOnClickListener(this);
        btnZhiyingAdd.setOnClickListener(this);
        tvCashin.setOnClickListener(this);
        upView.setOnClickListener(this);
        downView.setOnClickListener(this);
        tvSizeOther.setOnClickListener(this);
        tvMongey01.setOnClickListener(this);
        tvMongey02.setOnClickListener(this);
        tvMongey03.setOnClickListener(this);
        tvSize01.setOnClickListener(this);
        tvSize02.setOnClickListener(this);
        tvSize03.setOnClickListener(this);
        lineCheckUsecasher.setOnClickListener(this);
        checkUsequan.setOnClickListener(this);
    }


    /**
     * 止损的点数 和 钱数
     */
    private int zhiSun_value;
    private int zhiSun_money;
    /**
     * 止盈的点数 和 钱数
     */
    private int zhiYing_value;
    private int zhiYing_money;


    /**
     * 初始化views 数据
     *
     * @param isUp 买涨为 true 买跌为 false
     */
    private void initOrderPopwindowViews(boolean isUp) {

        //根据按钮点击情况，修改初始化配置
        if (isUp) {
            setViewBG(true);
            upView.setSelected(true);
            downView.setSelected(false);
        } else {
            setViewBG(false);
            downView.setSelected(true);
            upView.setSelected(false);
        }
        //初始化 seekbar
        initSeekbar();
       /* //初始化总价
        setTvTotalMoney();*/
    }

    /**
     * 初始化 止盈 止损的 seekbar
     */

    private void initSeekbar() {
        //初始化 止损seekbar
        seekBarZhisun.setMax(71);
//        seekBarZhisun.setMin(9);// setMin 方法属于新 APi26 才有
        seekBarZhisun.setProgress(0);
        tvZhisunValue.setText("不限");
        seekBarZhisun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.d("progress  :  " + progress);
                progress += 9;
                if (progress == 9) {
                    tvZhisunValue.setText("不限");
                    tvZhisunMoney.setText("");
                    return;
                }
                //止损点数 = progress
                zhiSun_value = progress;
                tvZhisunValue.setText(zhiSun_value + "点");

                // 止损钱数 = 10美元/手  * 1手 * progress / 100
                int i = leiXingPrice * countSize;
                LogUtils.d("leiXingPrice  : " + leiXingPrice);
                LogUtils.d("countSize  : " + countSize);
                float i1 = (float) i * progress / 100;
                tvZhisunMoney.setText("(" + "$ " + i1 + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // 止盈 seekbar
        seekBarZhiying.setMax(191);
//        seekBarZhiying.setMin(9);
        seekBarZhiying.setProgress(0);
        tvZhiyingValue.setText("不限");
        seekBarZhiying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.d("progress  :  " + progress);

                progress += 9;
                if (progress == 9) {
                    tvZhiyingValue.setText("不限");
                    tvZhiyingMoney.setText("");
                    return;
                }
                //止盈点数 = progress
                zhiYing_value = progress;
                tvZhiyingValue.setText(zhiYing_value + "点");

                // 止盈钱数 = 10美元/手  * 1手 * progress / 100
                int i = leiXingPrice * countSize;
                LogUtils.d("countSize  : " + countSize);
                float i1 = (float) i * progress / 100;
                tvZhiyingMoney.setText("(" + "$ " + i1 + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 设置显示 popwindow 时屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = baseActivity2.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        baseActivity2.getWindow().setAttributes(lp);
    }

    public static final int COUNT_SIZE01 = 1;
    public static final int COUNT_SIZE02 = 5;
    public static final int COUNT_SIZE03 = 10;

    public static final int MONEY01 = 8;
    public static final int MONEY02 = 80;
    public static final int MONEY03 = 200;
    /**
     * 记录建仓 ，几手 ，当前选中的为第几个，默认为第一个,
     */
    int countSize = 1;
    /**
     * 记录单价，即 多少钱一手,默认为 8
     */
    int leiXingPrice = 8;

    /**
     * 在没有点击其他按钮之前，根据点击的 条目，来更新  countSize
     */
    private void valueCountSize() {
        if (quickSizeView.getVisibility() == View.VISIBLE) {
            if (jianCangView.equals(tvSize01)) {
                countSize = COUNT_SIZE01;
            } else if (jianCangView.equals(tvSize02)) {
                countSize = COUNT_SIZE02;
            } else if (jianCangView.equals(tvSize03)) {
                countSize = COUNT_SIZE03;
            }
        }
    }

    /**
     * 记录多少钱一手 单价
     */
    private void valueLeiXing() {
        if (leiXingView.equals(tvMongey01)) {
            leiXingPrice = trade.getUnit_price_one();
        } else if (leiXingView.equals(tvMongey02)) {
            leiXingPrice = trade.getUnit_price_two();
        } else if (leiXingView.equals(tvMongey03)) {
            leiXingPrice = trade.getUnit_price_three();
        }
    }

    /**
     * 设置订单总价
     */
    private void setTvTotalMoney() {
        int totalMoney = leiXingPrice * countSize;
        int shouXufei = trade.getQuantity_commission_charges() * countSize;
        tvTotalMoney.setText("$" + (totalMoney + shouXufei)); //总金额
        tvOverNight.setText("过夜费" + totalMoney * trade.getQuantity_overnight_fee() + "美元/天，默认开启，建仓后可手动关闭"); // 过夜费
        tvFee.setText("（手续费：$" + shouXufei + "）"); //手续费
        int boDongNum = 0;
        if (leiXingPrice == trade.getUnit_price_one()) {
            boDongNum = trade.getQuantity_one();
        } else if (leiXingPrice == trade.getUnit_price_two()) {
            boDongNum = trade.getQuantity_two();
        } else {
            boDongNum = trade.getQuantity_three();
        }
        int heJiNum = boDongNum * countSize;
        textRatetips.setText("共计约合量" + heJiNum + ForeignUtil.formatForeignUtil(trade.getSymbolCode())
                + "每波动一个点，收益$" + trade.getQuantity_price_fluctuation() * countSize * heJiNum);
    }


    /**
     * 建仓 其他 按钮的点击事件
     */
    public void initJianCangOtherViews() {
        int index = 1;
        for (int i = 0; i < sizeOther01.getChildCount(); i++) {
            View v = sizeOther01.getChildAt(i);
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setText(index + "手");
                if (isUp) {
                    textView.setBackgroundResource(R.drawable.bg_item_quick_trade);
                    textView.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
                } else {
                    textView.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
                    textView.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
                }
                if (jianCangView == null)
                    jianCangView = textView;
                if (countSize == index)
                    jianCangView = textView;
                else
                    textView.setSelected(false);
                final int size = index;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textView = (TextView) view;
                        jianCangView.setSelected(false);
                        jianCangView = textView;
                        jianCangView.setSelected(true);
                        countSize = size;
                        //每次点击完之后重新初始化 seekbar
                        initSeekbar();
                        //重新设置总价
                        setTvTotalMoney();
                    }
                });
                index++;
            }
        }
        for (int i = 0; i < sizeOther02.getChildCount(); i++) {
            View v = sizeOther02.getChildAt(i);
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setText(index + "手");
                if (isUp) {
                    textView.setBackgroundResource(R.drawable.bg_item_quick_trade);
                    textView.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
                } else {
                    textView.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
                    textView.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
                }

                if (countSize == index)
                    jianCangView = textView;
                else
                    textView.setSelected(false);
                final int size = index;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textView = (TextView) view;
                        jianCangView.setSelected(false);
                        jianCangView = textView;
                        jianCangView.setSelected(true);
                        countSize = size;
                        //每次点击完之后重新初始化 seekbar
                        initSeekbar();
                        //重新设置总价
                        setTvTotalMoney();
                    }
                });
                index++;

            }
        }
        jianCangView.setSelected(true);
    }


    /**
     * 根据买涨买跌来改变背景色资源
     *
     * @param isUp
     */
    private void setViewBG(boolean isUp) {
        if (isUp) {
            tvMongey01.setBackgroundResource(R.drawable.bg_item_quick_trade);
            tvMongey02.setBackgroundResource(R.drawable.bg_item_quick_trade);
            tvMongey03.setBackgroundResource(R.drawable.bg_item_quick_trade);
            tvMongey01.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
            tvMongey02.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
            tvMongey03.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
            //根据“其他” 按钮点击与否，决定初始化不同的控件
            if (sizeOtherView.getVisibility() == View.VISIBLE) {
                //切换买涨买跌状态的时候，重新恢复默认
                countSize = 1;
                initJianCangOtherViews();
            } else {
                tvSize01.setBackgroundResource(R.drawable.bg_item_quick_trade);
                tvSize02.setBackgroundResource(R.drawable.bg_item_quick_trade);
                tvSize03.setBackgroundResource(R.drawable.bg_item_quick_trade);

                tvSize01.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
                tvSize02.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
                tvSize03.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
            }
            btnSubmit.setBackgroundResource(R.drawable.qucik_trade_submit);

        } else {
            tvMongey01.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
            tvMongey02.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
            tvMongey03.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
            tvMongey01.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
            tvMongey02.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
            tvMongey03.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
            //根据“其他” 按钮点击与否，决定初始化不同的控件
            if (sizeOtherView.getVisibility() == View.VISIBLE) {
                countSize = 1;

                initJianCangOtherViews();
            } else {

                tvSize01.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
                tvSize02.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
                tvSize03.setBackgroundResource(R.drawable.bg_item_quick_trade_down);

                tvSize01.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
                tvSize02.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
                tvSize03.setTextColor(baseActivity2.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));

            }
            btnSubmit.setBackgroundResource(R.drawable.qucik_trade_submit_green);
        }
        // 不管 “其他” 按钮点击与否，类型都要处理
        if (leiXingView != null) {
            leiXingView.setSelected(false);
        }
        leiXingView = tvMongey01;
        leiXingView.setSelected(true);

        //如果已经点击其他按钮，就直接返回
        if (sizeOtherView.getVisibility() == View.VISIBLE) {
            return;
        }
        //以下是没点击其他按钮时候的初始化操作
        if (jianCangView != null) {
            jianCangView.setSelected(false);
        }
        jianCangView = tvSize01;
        jianCangView.setSelected(true);
//        btnSubmit.setSelected(true);

    }

    /**
     * 设置最新价格，根据 netty socket 数据
     *
     * @param isGetNewPrice
     * @param price_new
     */
    public void setNewPrice(boolean isGetNewPrice, double price_new) {
        if (isGetNewPrice) {
            tvPrice01.setText(price_new + "");
        } else {
            tvPrice01.setText(price_buy);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gobackView://返回按钮
                break;
            case R.id.btn_submit://下单按钮
                transactionBuy();
                break;
            case R.id.line_check_usequan://代金券总布局
                checkUsequan.setImageResource(R.mipmap.img_choosequan_checked);
                checkUsecasher.setImageResource(R.mipmap.img_choosequan_normal);
                break;
            case R.id.btnZhisunLess://止损左侧减号
                seekBarZhisun.setProgress(seekBarZhisun.getProgress() - 1);
                break;
            case R.id.btnZhisunAdd://止损右侧加号
                seekBarZhisun.setProgress(seekBarZhisun.getProgress() + 1);
                break;
            case R.id.btnZhiyingLess://止盈左侧减号
                seekBarZhiying.setProgress(seekBarZhiying.getProgress() - 1);
                break;
            case R.id.btnZhiyingAdd://止盈右侧加号
                seekBarZhiying.setProgress(seekBarZhiying.getProgress() + 1);
                break;
            case R.id.tv_cashin://充值
                popupWindow_order.dismiss();
                //跳转充值页面
                Intent intent = new Intent(baseActivity2, PayActivity.class);
                baseActivity2.startActivity(intent);
                break;
            case R.id.upView://买涨
                isUp = true;
                upView.setSelected(true);
                downView.setSelected(false);
                setViewBG(true);
                //重新初始化单价
                valueLeiXing();
                //更改总价
                setTvTotalMoney();
                break;
            case R.id.downView://买跌
                isUp = false;
                downView.setSelected(true);
                upView.setSelected(false);
                setViewBG(false);
                //重新初始化单价
                valueLeiXing();
                //更改总价
                setTvTotalMoney();
                break;
            case R.id.tv_sizeOther://其他
                LogUtils.d("其他  点击事件");
                valueCountSize();
                quickSizeView.setVisibility(View.GONE);
                initJianCangOtherViews();
                sizeOtherView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_mongey01://价格一 8元/手
            case R.id.tv_mongey02://价格二
            case R.id.tv_mongey03://价格三
                //当前 leiXingView ·为上一个 选中的 view
                leiXingView.setSelected(false);
                //改变为当前的view
                leiXingView = v;
                //修改当前 view 状态
                leiXingView.setSelected(true);
                //记录多少钱一手
                valueLeiXing();
                //每次点击完之后重新初始化 seekbar
                initSeekbar();
                //重新设置总价
                setTvTotalMoney();
                break;

            case R.id.tv_size01://数量一 1 手
            case R.id.tv_size02://数量二
            case R.id.tv_size03://数量三
                jianCangView.setSelected(false);
                jianCangView = v;
                jianCangView.setSelected(true);
                //记录点击的手数
                valueCountSize();
                //每次点击完之后重新初始化 seekbar
                initSeekbar();
                //重新设置总价
                setTvTotalMoney();
                break;
            case R.id.line_check_usecasher://余额总布局
                LogUtils.d("余额总布局点击事件");
                checkUsecasher.setImageResource(R.mipmap.img_choosequan_checked);
                checkUsequan.setImageResource(R.mipmap.img_choosequan_normal);
                break;
            case R.id.check_usequan:
                checkUsecasher.setImageResource(R.mipmap.img_choosequan_normal);
                checkUsequan.setImageResource(R.mipmap.img_choosequan_checked);
                break;

        }
    }

    private void transactionBuy() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", SharePreferencesUtil.getInstance().getUserPhone());
        map.put("transactionStatus", "1"); //1 建仓
        map.put("symbolCode", symbolCode);
        map.put("ransactionType", isUp ? "1" : "2");//1买张 2买跌
        map.put("unitPrice", leiXingPrice);//建仓单价
        map.put("lot", countSize);
        map.put("id", 0);
        map.put("stopLossCount", zhiSun_value);
        map.put("stopProfitCount", zhiYing_value);
        map.put("url", "/transaction/buy");
        try {
            map.put("sign", RSAKeyManger.sign(RSAKeyManger.getFormatParams(map)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServerManger.getInstance().getServer().transactionBuy(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult<String> stringServerResult) {
                        ToastUtils.showShort("购买成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("购买失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public boolean isShow() {
        return popupWindow_order.isShowing();
    }
}
