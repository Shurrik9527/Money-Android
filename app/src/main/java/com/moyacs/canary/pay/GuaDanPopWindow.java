package com.moyacs.canary.pay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.RSAKeyManger;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.ForeignUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import www.moyacs.com.myapplication.R;

public class GuaDanPopWindow implements View.OnClickListener {
    private PopupWindow guaDanWindow;
    private Activity activity;
    private TextView tvBuyUp; // 买涨
    private TextView tvBuyDown;//买跌
    private TextView tvMongey01;//多少钱一手 ，价格一
    private TextView tvMongey02;//多少钱一手 ，价格二
    private TextView tvMongey03;//多少钱一手 ，价格三
    private TextView tvSize01; //1手
    private TextView tvSize02;//5 手
    private TextView tvSize03;//10手
    private TextView tvSizeOther; //其他
    private LinearLayout quickSizeView; //建仓第一行布局，1手 5手 10手 其他
    private LinearLayout sizeOther01; //点击其他之后显示出来的建仓第二行布局
    private LinearLayout sizeOther02; //点击其他之后显示出来的建仓第三行布局
    private LinearLayout sizeOtherView;//点击其他之后显示的建仓总布局
    private TextView tvZhisunValue; //止损点数
    private TextView tvZhisunMoney; //止损价格
    private Button btnZhisunLess; //止损左侧 减号按钮
    private SeekBar seekBarZhisun; //止损 seekbar
    private Button btnZhisunAdd; //止损右侧 加号 btn
    private TextView tvZhiyingValue; //止盈点数
    private TextView tvZhiyingMoney;//止盈价格
    private Button btnZhiyingLess;//止盈 左侧 点击按钮
    private SeekBar seekBarZhiying;//止盈 seekbar
    private Button btnZhiyingAdd;//止盈 右侧 加号 按钮
    private TextView tvTotalMoney;//总价
    private TextView tvTradeName; // 产品名字
    private TextView tvTradeValue;//产品价格
    private Button btnSubmit; // 下单
    private TextView tvNightFee; //过夜费
    private TextView tvFee; //手续费
    private TextView tvRateTips; // 波动提示
    private LotEditText etGuaDanLot;// 挂单浮动
    private MoneyEditText etGuaDanPic;//挂单单价
    private TextView tvRange;//挂单范围


    private List<TextView> textViewList;
    private View oldMoneyView, oldSizeView; // 旧的单价  旧的数量
    private int buySize = 0;//购买了多少手
    private int buyUnity = 0; //购买单价
    private int zhiSun_value;//止损的点数 和 钱数
    private int zhiYing_value;//止盈的点数 和 钱数
    private String symbolName;//产品价格
    private String symbolCode;
    private String priceBuy;// 买入价
    private int textColor;//涨幅颜色
    private TradeVo.Trade trade;
    private boolean isUp = true; // 买涨还是买跌

    public GuaDanPopWindow(Activity activity, String price_buy, String symbol_cn, int textColor, String symbolCode, TradeVo.Trade trade) {
        this.activity = activity;
        this.symbolName = symbol_cn;
        this.symbolCode = symbolCode;
        this.priceBuy = price_buy;
        this.textColor = textColor;
        this.trade = trade;
        initWindow();
    }

    /**
     * 初始化弹框
     */
    private void initWindow() {
        View popView = View.inflate(activity, R.layout.dialog_gua_dan, null);
        //初始化 view 相关数据 和 参数
        guaDanWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT,
                activity.getResources().getDisplayMetrics().heightPixels * 4 / 5,
//                ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * 4 / 5,
                true);
        //设置背景色
        guaDanWindow.setBackgroundDrawable(new ColorDrawable(0));
        //动画效果
        guaDanWindow.setAnimationStyle(R.style.popwin_anim_style);
        guaDanWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        guaDanWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //点击空白位置，popupwindow消失的事件监听，这时候让背景恢复正常
        guaDanWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        initView(popView);
        initSeekbar();
    }

    private void initView(View view) {
        tvTradeName = view.findViewById(R.id.tv_tradeName);
        tvTradeValue = view.findViewById(R.id.tv_tradeValue);
        tvBuyUp = view.findViewById(R.id.tv_buyUp);
        tvBuyDown = view.findViewById(R.id.tv_buyDown);
        tvMongey01 = view.findViewById(R.id.tv_mongey01);
        tvMongey02 = view.findViewById(R.id.tv_mongey02);
        tvMongey03 = view.findViewById(R.id.tv_mongey03);
        tvSize01 = view.findViewById(R.id.tv_size01);
        tvSize02 = view.findViewById(R.id.tv_size02);
        tvSize03 = view.findViewById(R.id.tv_size03);
        tvSizeOther = view.findViewById(R.id.tv_sizeOther);
        quickSizeView = view.findViewById(R.id.quickSizeView);
        sizeOther01 = view.findViewById(R.id.sizeOther01);
        sizeOther02 = view.findViewById(R.id.sizeOther02);
        sizeOtherView = view.findViewById(R.id.sizeOtherView);
        tvZhisunValue = view.findViewById(R.id.tv_zhisunValue);
        tvZhisunMoney = view.findViewById(R.id.tv_zhisunMoney);
        btnZhisunLess = view.findViewById(R.id.btnZhisunLess);
        seekBarZhisun = view.findViewById(R.id.seekBarZhisun);
        btnZhisunAdd = view.findViewById(R.id.btnZhisunAdd);
        tvZhiyingValue = view.findViewById(R.id.tv_zhiyingValue);
        tvZhiyingMoney = view.findViewById(R.id.tv_zhiyingMoney);
        btnZhiyingLess = view.findViewById(R.id.btnZhiyingLess);
        seekBarZhiying = view.findViewById(R.id.seekBarZhiying);
        btnZhiyingAdd = view.findViewById(R.id.btnZhiyingAdd);
        tvTotalMoney = view.findViewById(R.id.tv_totalMoney);
        btnSubmit = view.findViewById(R.id.btn_submit);
        tvNightFee = view.findViewById(R.id.tv_nightFee);
        tvFee = view.findViewById(R.id.tv_fee);
        tvRateTips = view.findViewById(R.id.tv_rateTips);
        etGuaDanLot = view.findViewById(R.id.et_guaDanLot);
        etGuaDanPic = view.findViewById(R.id.et_guaDanPic);
        tvRange = view.findViewById(R.id.tv_range);
        //充值
        TextView tvRecharge = view.findViewById(R.id.tv_recharge);

        textViewList = new ArrayList<>();
        textViewList.add(tvMongey01);
        textViewList.add(tvMongey02);
        textViewList.add(tvMongey03);
        textViewList.add(tvSize01);
        textViewList.add(tvSize02);
        textViewList.add(tvSize03);

        tvBuyUp.setOnClickListener(this);
        tvBuyDown.setOnClickListener(this);
        tvSizeOther.setOnClickListener(this);
        tvMongey01.setOnClickListener(this);
        tvMongey02.setOnClickListener(this);
        tvMongey03.setOnClickListener(this);
        tvSize01.setOnClickListener(this);
        tvSize02.setOnClickListener(this);
        tvSize03.setOnClickListener(this);
        btnZhisunLess.setOnClickListener(this);
        btnZhisunAdd.setOnClickListener(this);
        btnZhiyingLess.setOnClickListener(this);
        btnZhiyingAdd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etGuaDanPic.setTextChangeListener(new MoneyEditText.TextChangeListener() {
            @Override
            public void textChangeCallBack(float textValue) {
                float startValue = textValue - etGuaDanLot.getTextValue();
                float endValue = etGuaDanLot.getTextValue() + textValue;
                tvRange.setText("成交价格范围：" + startValue + "~" + endValue);
            }
        });
        etGuaDanLot.setTextChangeListener(new LotEditText.TextChangeListener() {
            @Override
            public void onTextChangeCallBack(int lot) {
                float startValue = etGuaDanPic.getTextValue() - lot;
                float endValue = lot + etGuaDanPic.getTextValue();
                tvRange.setText("成交价格范围：" + startValue + "~" + endValue);
            }
        });
        setValue();
    }

    private void setValue() {
        tvMongey01.setText(trade.getUnit_price_one() + "美元/手");
        tvMongey02.setText(trade.getUnit_price_two() + "美元/手");
        tvMongey03.setText(trade.getUnit_price_three() + "美元/手");
        tvTradeValue.setTextColor(textColor);
        tvTradeValue.setText(priceBuy);
        tvTradeName.setText(symbolName);

        onClick(tvBuyUp);
        onClick(tvMongey01);
        onClick(tvSize01);
    }

    public void showWindow(View view) {
        //设置背景半透明
        backgroundAlpha(0.6f);
        //位置
        guaDanWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    private void initSeekbar() {
        //初始化 止损seekbar
        seekBarZhisun.setMax(71);
        seekBarZhisun.setProgress(0);
        tvZhisunValue.setText("不限");
        seekBarZhisun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
                int i = buyUnity * buySize;
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
        seekBarZhiying.setProgress(0);
        tvZhiyingValue.setText("不限");
        seekBarZhiying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
                int i = buyUnity * buySize;
                LogUtils.d("countSize  : " + buySize);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_buyUp:
                tvBuyUp.setSelected(true);
                tvBuyDown.setSelected(false);
                setViewColor(true);
                isUp = true;
                break;
            case R.id.tv_buyDown:
                tvBuyDown.setSelected(true);
                tvBuyUp.setSelected(false);
                setViewColor(false);
                isUp = false;
                break;
            case R.id.tv_mongey01:
                buyUnity = trade.getUnit_price_one();
                setMoneyView(v);
                break;
            case R.id.tv_mongey02:
                buyUnity = trade.getUnit_price_two();
                setMoneyView(v);
                break;
            case R.id.tv_mongey03:
                buyUnity = trade.getUnit_price_three();
                setMoneyView(v);
                break;
            case R.id.tv_size01:
                buySize = 1;
                setSizeView(v);
                break;
            case R.id.tv_size02:
                buySize = 5;
                setSizeView(v);
                break;
            case R.id.tv_size03:
                buySize = 10;
                setSizeView(v);
                break;
            case R.id.tv_sizeOther:
                quickSizeView.setVisibility(View.GONE);
                sizeOtherView.setVisibility(View.VISIBLE);
                initJianCangOtherViews(tvBuyUp.isSelected());
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
            case R.id.btn_submit:
                reserveTransaction();
                break;
            case R.id.tv_recharge:
                //跳转充值页面
                Intent intent = new Intent(activity, PayActivity.class);
                activity.startActivity(intent);
                guaDanWindow.dismiss();
                break;
        }
    }

    private void setMoneyView(View view) {
        if (oldMoneyView != null) {
            oldMoneyView.setSelected(false);
        }
        view.setSelected(true);
        oldMoneyView = view;
        setMoneyValue();
    }

    private void setSizeView(View view) {
        if (oldSizeView != null) {
            oldSizeView.setSelected(false);
        }
        view.setSelected(true);
        oldSizeView = view;
        setMoneyValue();
    }

    private void setMoneyValue() {
        tvTotalMoney.setText("$ " + buySize * buyUnity);
        int totalMoney = buyUnity * buySize;
        tvNightFee.setText("过夜费" + totalMoney * trade.getQuantity_overnight_fee() + "美元/天，默认开启，建仓后可手动关闭"); // 过夜费
        int shouXufei = trade.getQuantity_commission_charges() * buySize;
        tvFee.setText("（手续费：$" + shouXufei + "）"); //手续费
        int boDongNum = 0;
        if (buyUnity == trade.getUnit_price_one()) {
            boDongNum = trade.getQuantity_one();
        } else if (buyUnity == trade.getUnit_price_two()) {
            boDongNum = trade.getQuantity_two();
        } else {
            boDongNum = trade.getQuantity_three();
        }
        int heJiNum = boDongNum * buySize;
        tvRateTips.setText("共计约合量" + heJiNum + ForeignUtil.formatForeignUtil(trade.getSymbolCode())
                + "每波动一个点，收益$" + trade.getEntryOrders() * buySize * heJiNum);
    }

    private void initJianCangOtherViews(boolean isUp) {
        if (quickSizeView.getVisibility() == View.VISIBLE) {
            return;
        }
        int index = 1;
        for (int i = 0; i < sizeOther01.getChildCount(); i++) {
            View v = sizeOther01.getChildAt(i);
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setText(index + "手");
                if (isUp) {
                    textView.setBackgroundResource(R.drawable.bg_item_quick_trade);
                    textView.setTextColor(activity.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
                } else {
                    textView.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
                    textView.setTextColor(activity.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
                }
                final int size = index;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buySize = size;
                        setSizeView(v);
                    }
                });
            }
            index++;
        }
        for (int i = 0; i < sizeOther02.getChildCount(); i++) {
            View v = sizeOther02.getChildAt(i);
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setText(index + "手");
                if (isUp) {
                    textView.setBackgroundResource(R.drawable.bg_item_quick_trade);
                    textView.setTextColor(activity.getResources().getColorStateList(R.color.item_quick_trade_color_tv));
                } else {
                    textView.setBackgroundResource(R.drawable.bg_item_quick_trade_down);
                    textView.setTextColor(activity.getResources().getColorStateList(R.color.item_quick_trade_color_tv_down));
                }
                final int size = index;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buySize = size;
                        setSizeView(v);
                    }
                });
                index++;
            }
        }
    }

    /**
     * 设置选择View的颜色
     */
    private void setViewColor(boolean isUp) {
        for (TextView tv : textViewList) {
            tv.setBackgroundResource(isUp ? R.drawable.bg_item_quick_trade : R.drawable.bg_item_quick_trade_down);
            tv.setTextColor(activity.getResources().getColorStateList(isUp ? R.color.item_quick_trade_color_tv : R.color.item_quick_trade_color_tv_down));
        }
        initJianCangOtherViews(isUp);
        btnSubmit.setBackgroundResource(isUp ? R.drawable.qucik_trade_submit : R.drawable.qucik_trade_submit_green);
    }

    /**
     * 更新数据
     */
    public void setNewPrice(boolean isGetNewPrice, String price_new) {
        if (isGetNewPrice) {
            tvTradeValue.setText(price_new);
        } else {
            tvTradeValue.setText(priceBuy);
        }
    }

    private void reserveTransaction() {
        String pic = etGuaDanPic.getText().toString();
        if (TextUtils.isEmpty(pic)) {
            ToastUtils.showShort("请输入挂单价格");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", SPUtils.getInstance().getString(AppConstans.USER_PHONE));
        map.put("transactionStatus", "1"); // 1 是建仓
        map.put("symbolCode", symbolCode);
        map.put("ransactionType", isUp ? "1" : "2");//1是涨 2是跌
        map.put("unitPrice", buyUnity);
        map.put("lot", buySize);
        map.put("id", 0);
        map.put("stopLossCount", zhiSun_value);
        map.put("stopProfitCount", zhiYing_value);
        map.put("entryOrdersPrice", pic);
        map.put("errorRange", etGuaDanLot.getText().toString());
        map.put("url", "/transaction/reserve");
        try {
            map.put("sign", RSAKeyManger.sign(RSAKeyManger.getFormatParams(map)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServerManger.getInstance().getServer().reserveTransaction(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult stringServerResult) {
                        if (stringServerResult.isSuccess()) {
                            ToastUtils.showShort("挂单成功");
                        } else {
                            onError(new Throwable("系统服务错误，请稍后再试"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
