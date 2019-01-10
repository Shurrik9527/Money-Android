package com.moyacs.canary.pay;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.common.StringUtil;
import com.moyacs.canary.pay.contract.PayContract;
import com.moyacs.canary.pay.contract.PayPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/4/12 0012 11:02
 * 邮箱：rsf411613593@gmail.com
 * 说明：市价单 新版
 */

public class ShiJiaDanFragment2 extends BaseFragment2 implements PayContract.PayView {

    Unbinder unbinder;
    /**
     * 买涨文字
     */
    @BindView(R.id.textUpView)
    TextView textUpView;
    /**
     * 买涨按钮
     */
    @BindView(R.id.upView)
    RelativeLayout upView;
    /**
     * 买跌文字
     */
    @BindView(R.id.textDownView)
    TextView textDownView;
    /**
     * 买跌按钮
     */
    @BindView(R.id.downView)
    RelativeLayout downView;
    /**
     * 建仓手数 减号
     */
    @BindView(R.id.lessnumImg)
    ImageView lessnumImg;
    /**
     * 建仓手数 减号布局
     */
    @BindView(R.id.lessnumView)
    RelativeLayout lessnumView;
    /**
     * 建仓手数 输入的内容
     */
    @BindView(R.id.ed_num)
    EditText edNum;
    /**
     * 建仓手数 加号
     */
    @BindView(R.id.addnumImg)
    ImageView addnumImg;
    /**
     * 建仓手数 加号布局
     */
    @BindView(R.id.addnumView)
    RelativeLayout addnumView;
    @BindView(R.id.numRView)
    LinearLayout numRView;
    /**
     * 止损价格 左侧的 对号
     */
    @BindView(R.id.checkLoss)
    ImageView checkLoss;
    /**
     * 止损价格布局
     */
    @BindView(R.id.lossViewLeft)
    LinearLayout lossViewLeft;
    /**
     * 止损价格  减号
     */
    @BindView(R.id.lessLossImg)
    ImageView lessLossImg;
    /**
     * 止损价格 减号布局
     */
    @BindView(R.id.lessLossView)
    RelativeLayout lessLossView;
    /**
     * 止损价格输入的内容
     */
    @BindView(R.id.ed_loss)
    EditText edLoss;
    /**
     * 止损价格 加号
     */
    @BindView(R.id.addLossImg)
    ImageView addLossImg;
    /**
     * 止损价格  加号布局
     */
    @BindView(R.id.addLossView)
    RelativeLayout addLossView;
    @BindView(R.id.lossViewRight)
    LinearLayout lossViewRight;
    @BindView(R.id.tv_lossHint)
    TextView tvLossHint;
    /**
     * 止盈价格 对号
     */
    @BindView(R.id.checkPorfit)
    ImageView checkPorfit;
    /**
     * 止盈价格 左侧布局
     */
    @BindView(R.id.profitViewLeft)
    LinearLayout profitViewLeft;
    /**
     * 止盈价格 减号
     */
    @BindView(R.id.lessProfitImg)
    ImageView lessProfitImg;
    /**
     * 止盈价格  减号 布局
     */
    @BindView(R.id.lessProfitView)
    RelativeLayout lessProfitView;
    /**
     * 止盈价格输入内容
     */
    @BindView(R.id.ed_profit)
    EditText edProfit;
    /**
     * 止盈价格 加号
     */
    @BindView(R.id.addProfitImg)
    ImageView addProfitImg;
    /**
     * 止盈价格 加号布局
     */
    @BindView(R.id.addProfitView)
    RelativeLayout addProfitView;
    @BindView(R.id.profitViewRight)
    LinearLayout profitViewRight;
    @BindView(R.id.tv_profitHint)
    TextView tvProfitHint;
    /**
     * 余额 “---元”
     */
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.line_check_usecasher)
    LinearLayout lineCheckUsecasher;
    /**
     * 充值按钮
     */
    @BindView(R.id.tv_cashin)
    TextView tvCashin;
    /**
     * 下单按钮
     */
    @BindView(R.id.btn_order)
    Button btnOrder;
    /**
     * 显示买入价
     */
    @BindView(R.id.tv_price_buy)
    TextView tvPriceBuy;
    /**
     * 显示卖出价
     */
    @BindView(R.id.tv_price_sell)
    TextView tvPriceSell;
    private GradientDrawable gradientDrawable;
    /**
     * 1 买涨 ， 2 买跌
     */
    private int type_order;
    /**
     * 小数点位数
     */
    private int digit;
    /**
     * 止损 止盈 点位
     */
    private int stops_level;
    /**
     * 计算止损 止盈 时候的 加减值
     */
    private double range;
    /**
     * 止损 止盈，加减号时，每次的幅度
     */
    private double range_change;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {

        View rootView = inflater.inflate(R.layout.fragment_shijiadan_2, null);
        unbinder = ButterKnife.bind(this, rootView);
        initViews();
        initEditText();
        return rootView;
    }


    /**
     * 初始化 相关view
     */
    private void initViews() {
        tvPriceBuy.setText(price_buy);
        tvPriceSell.setText(price_sell);

        //初始化买涨买跌背景色
        if (type_order == 1) {
            setBg_maiZhang();
        } else {
            setBg_maiDie();
        }
        //初始化 止损 止盈的选中状态
        //止损
        lossViewLeft.setSelected(false);
        lessLossView.setEnabled(false);
        lessLossImg.setEnabled(false);
        addLossView.setEnabled(false);
        addLossImg.setEnabled(false);
        edLoss.setEnabled(false);
        //止盈
        profitViewLeft.setSelected(false);
        lessProfitView.setEnabled(false);
        lessProfitImg.setEnabled(false);
        addProfitView.setEnabled(false);
        addProfitImg.setEnabled(false);
        edProfit.setEnabled(false);
    }

    /**
     * 初始化 editText
     */
    private void initEditText() {
        //手数
        edNum.setText("0.01");
        edNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 2 + 1);
                        edNum.setText(s);
                        edNum.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edNum.setText(s);
                    edNum.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edNum.setText(s.subSequence(0, 1));
                        edNum.setSelection(1);
                        return;
                    }
                }
                //不能以小数点结尾
//                if (s.toString().endsWith(".")) {
//                    edNum.setText(edNum.getText().toString()+"0");
//                    edNum.setSelection(edNum.getText().toString().length());
//                }

                String s1 = s.toString();
                if (s1 == null || s1.equals("")) {
                    return;
                }

                Double valueOf = Double.valueOf(s1);
                if (valueOf > 20) {
                    edNum.setText("20.00");
                    edNum.clearFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //止损
        edLoss.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edLoss.setText(s);
                    edLoss.setSelection(2);
                }

                String s1 = s.toString();
                if (s1 == null || s1.equals("")) {
                    return;
                }

                Double valueOf = Double.valueOf(s1);
                if (zhiSunValue == null || zhiSunValue.equals("") || zhiSunValue.equals("null")) {
                    return;
                }
                Double zhiSunValue_d = Double.valueOf(zhiSunValue);
                if (type_order == 1) {

                    if (valueOf > zhiSunValue_d) {
                        edLoss.setText(zhiSunValue_d + "");

                    }
                } else {
                    if (valueOf < zhiSunValue_d) {
                        edLoss.setText(zhiSunValue);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //止盈
        edProfit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edProfit.setText(s);
                    edProfit.setSelection(2);
                }

                String s1 = s.toString();
                if (s1 == null || s1.equals("")) {
                    return;
                }

                Double valueOf = Double.valueOf(s1);
                if (zhiYingValue == null || zhiYingValue.equals("") || zhiYingValue.equals("null")) {
                    return;
                }
                Double zhiYingValue_d = Double.valueOf(zhiYingValue);
                if (type_order == 1) {

                    if (valueOf < zhiYingValue_d) {
                        edProfit.setText(zhiYingValue);

                    }
                } else {
                    if (valueOf > zhiYingValue_d) {
                        edProfit.setText(zhiYingValue);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 点击买涨时候的背景色设置
     */
    private void setBg_maiZhang() {
        upView.setSelected(true);
        downView.setSelected(false);
//        setBtnBackground(getResources().getColor(R.color.color_opt_gt));

    }

    /**
     * 点击买跌时候的背景色设置
     */
    private void setBg_maiDie() {
        downView.setSelected(true);
        upView.setSelected(false);
//        setBtnBackground(getResources().getColor(R.color.color_opt_lt));
    }


    @Override
    protected void initBundleData(Bundle bundle) {
        type_order = getArguments().getInt("type_order");
        digit = getArguments().getInt("digit");
        stops_level = getArguments().getInt("stops_level");
        price_buy = getArguments().getString("price_buy");
        price_sell = getArguments().getString("price_sell");
        symbol = getArguments().getString("symbol");


        switch (digit) {
            case 1:
                range_change = 0.1;
                range = NumberUtils.multiply(stops_level, range_change);
                break;
            case 2:
                range_change = 0.01;
                range = NumberUtils.multiply(stops_level, range_change);
                break;
            case 3:
                range_change = 0.001;
                range = NumberUtils.multiply(stops_level, range_change);
                break;
            case 4:
                range_change = 0.0001;
                range = NumberUtils.multiply(stops_level, range_change);
                break;
            case 5:
                range_change = 0.00001;
                range = NumberUtils.multiply(stops_level, range_change);
                break;
        }
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 设置下单按钮背景色
     *
     * @param color
     */
    private void setBtnBackground(int color) {
        if (gradientDrawable == null) {

            gradientDrawable = (GradientDrawable) btnOrder.getBackground();
        }
        gradientDrawable.setColor(color);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setZhiSunAndZhiYing(price_buy, price_sell);
    }


    @OnClick({R.id.upView, R.id.downView, R.id.lessnumView, R.id.addnumView, R.id.lossViewLeft,
            R.id.lessLossView, R.id.addLossView, R.id.profitViewLeft, R.id.lessProfitView,
            R.id.addProfitView, R.id.tv_cashin, R.id.btn_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upView://买涨

                type_order = 1;
                //设置买涨按钮背景
                setBg_maiZhang();

                OnZhangOrDieClick();

                break;
            case R.id.downView://买跌
                type_order = 2;
                setBg_maiDie();
                OnZhangOrDieClick();


                break;
            case R.id.lessnumView://手数 减
                String s = edNum.getText().toString();
                if (s == null || s.equals("")) {
                    edNum.setText("0.01");
                    edNum.setSelection(4);
                    return;
                } else {
                    Double aDouble = Double.valueOf(s);
                    if (aDouble <= 0.01) {
                        edNum.setText(0.01 + "");
                        edNum.setSelection(4);
                        return;
                    }
                    double subtract = NumberUtils.subtract(aDouble, 0.01);
                    String s1 = NumberUtils.setScale(subtract, 2);
                    edNum.setText(s1);
                    edNum.setSelection(edNum.getText().toString().trim().length());
                }
                break;
            case R.id.addnumView://手数 加
                String s2 = edNum.getText().toString();
                if (s2 == null || s2.equals("")) {
                    edNum.setText("0.01");
                    edNum.setSelection(4);
                    return;
                } else {
                    Double aDouble = Double.valueOf(s2);
                    if (aDouble < 0.01) {
                        edNum.setText(0.01 + "");
                        edNum.setSelection(edNum.getText().toString().trim().length());
                        return;
                    }
                    if (aDouble >= 20) {
                        edNum.setText("20.00");
                        edNum.setSelection(edNum.getText().toString().trim().length());
                        return;
                    }
                    double subtract = NumberUtils.add(aDouble, 0.01);
                    String s1 = NumberUtils.setScale(subtract, 2);
                    edNum.setText(s1);
                    edNum.setSelection(edNum.getText().toString().trim().length());
                }
                break;
            case R.id.lossViewLeft://是否选择止损价格
                if (lossViewLeft.isSelected()) {
                    lossViewLeft.setSelected(false);
                    //如果止损价格未选中，则  加减号设置为不可点击
                    lessLossView.setEnabled(false);
                    lessLossImg.setEnabled(false);
                    addLossView.setEnabled(false);
                    addLossImg.setEnabled(false);

                    edLoss.setEnabled(false);

                    edLoss.setText("");

                } else {
                    lossViewLeft.setSelected(true);

                    lessLossView.setEnabled(true);
                    lessLossImg.setEnabled(true);
                    addLossView.setEnabled(true);
                    addLossImg.setEnabled(true);

                    edLoss.setEnabled(true);
                    edLoss.setText(zhiSunValue);
                }
                break;
            case R.id.lessLossView:// 止损价格 减
                setLossEditText(false);
                break;
            case R.id.addLossView:// 止损价格 加
                setLossEditText(true);
                break;
            case R.id.profitViewLeft://是否选择止盈价格
                if (profitViewLeft.isSelected()) {
                    profitViewLeft.setSelected(false);

                    lessProfitView.setEnabled(false);
                    lessProfitImg.setEnabled(false);
                    addProfitView.setEnabled(false);
                    addProfitImg.setEnabled(false);
                    edProfit.setEnabled(false);
                    edProfit.setText("");
                } else {
                    profitViewLeft.setSelected(true);

                    lessProfitView.setEnabled(true);
                    lessProfitImg.setEnabled(true);
                    addProfitView.setEnabled(true);
                    addProfitImg.setEnabled(true);

                    edProfit.setEnabled(true);
                    edProfit.setText(zhiYingValue);

                }
                break;
            case R.id.lessProfitView://止盈 减
                setProfitEditText(false);
                break;
            case R.id.addProfitView:// 止盈 加
                setProfitEditText(true);
                break;
            case R.id.tv_cashin://充值
                long nowMills = TimeUtils.getNowMills();
                String url = "http://uc.moyacs.com/my.account-deposit.funds_app_v2.html?v=" + nowMills + "&mt4id=812999&token=xxxxxxx";
                Intent intent = new Intent(getContext(), PayActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.btn_order://下单
                //mt4 id
                int mt4id = SPUtils.getInstance().getInt(AppConstans.mt4id);
                //server
                String server = SPUtils.getInstance().getString(AppConstans.type);
                //type
                String type = null;
                if (type_order == 1) {
                    type = "BUY";
                } else {
                    type = "SELL";
                }
                String trim = edNum.getText().toString().trim();
                trim = StringUtil.endWithPoint(trim);
                Double volume = Double.valueOf(trim);
                //手数，因为 mt4 服务器接收的是 int 所以乘以 100
                Double multiply = NumberUtils.multiply(volume, 100);
                PayPresenterImpl payPresenter = new PayPresenterImpl(this);
                //止损价
                double sl;
                if (lossViewLeft.isSelected()) {
                    String trim1 = edLoss.getText().toString().trim();
                    trim1 = StringUtil.endWithPoint(trim1);
                    sl = Double.valueOf(trim1);
                } else {
                    sl = 0D;
                }
                //止盈价格
                double tp;
                if (profitViewLeft.isSelected()) {
                    String trim1 = edProfit.getText().toString().trim();
                    trim1 = StringUtil.endWithPoint(trim1);
                    tp = Double.valueOf(trim1);
                } else {
                    tp = 0D;
                }
//                payPresenter.closeOrder(server, mt4id, symbol, type, multiply.intValue(), sl, tp, null, 0, "");
                Log.i("mt4id", "mt4id: " + mt4id + "      server : " + server);
                break;
        }
    }

    /**
     * 止损价格  根据 加减来计算 对应 edittext 的 值
     *
     * @param isLossViewAdd
     */
    private void setLossEditText(boolean isLossViewAdd) {
        String trim = edLoss.getText().toString().trim();
        if (trim == null || trim.equals("")) {
            edProfit.setText(zhiSunValue);
            trim = zhiSunValue;
        }
        if (zhiSunValue == null || zhiSunValue.equals("") || zhiSunValue.equals("null")) {
            return;
        }
        Double aDouble = Double.valueOf(trim);
        double subtract = 0;
        if (isLossViewAdd) {
            subtract = NumberUtils.add(aDouble, range_change);
        } else {

            subtract = NumberUtils.subtract(aDouble, range_change);
        }
        String s1 = NumberUtils.setScale(subtract, digit);
        edLoss.setText(s1);
        edLoss.setSelection(edLoss.getText().toString().trim().length());


    }


    /**
     * 止盈价格  根据 加减来计算 对应 edittext 的 值
     *
     * @param isProfitViewAdd
     */
    private void setProfitEditText(boolean isProfitViewAdd) {
        String trim = edProfit.getText().toString().trim();
        if (trim == null || trim.equals("")) {
            edProfit.setText(zhiYingValue);
            trim = zhiYingValue;
        }
        if (zhiYingValue == null || zhiYingValue.equals("") || zhiYingValue.equals("null")) {
            return;
        }
        Double aDouble = Double.valueOf(trim);
        double subtract = 0;
        if (isProfitViewAdd) {
            subtract = NumberUtils.add(aDouble, range_change);
        } else {

            subtract = NumberUtils.subtract(aDouble, range_change);
        }
        String s1 = NumberUtils.setScale(subtract, digit);
        edProfit.setText(s1);

        edProfit.setSelection(edProfit.getText().toString().trim().length());
    }

    /**
     * 买涨  买跌 按钮点击事件处理（通用部分）
     */
    private void OnZhangOrDieClick() {

        //重新计算止盈止损价格
        setZhiSunAndZhiYing(price_buy, price_sell);
        //根据止盈止损是否为点击状态，改变 输入框的内容
        if (lossViewLeft.isSelected()) {
            edLoss.setText(zhiSunValue);
            edLoss.setSelection(edLoss.getText().toString().trim().length());
        } else {
            edLoss.setText("");
        }
        if (profitViewLeft.isSelected()) {
            edProfit.setText(zhiSunValue);
            edProfit.setSelection(edProfit.getText().toString().trim().length());
        } else {
            edProfit.setText("");
        }
    }

    /**
     * 传递过来的 买入价，防止价格波动过慢，数据显示不出来
     */
    private String price_buy;
    /**
     * 传递过来的 卖出价
     */
    private String price_sell;
    /**
     * 传递过来的品种名称
     */
    private String symbol;
    //止损值
    String zhiSunValue = null;
    //止盈值
    String zhiYingValue = null;

    /**
     * 设置最新买入价和卖出价
     *
     * @param price_buy
     * @param price_sell
     */
    public void setPrice(String price_buy, String price_sell) {
        this.price_buy = price_buy;
        this.price_sell = price_sell;
        tvPriceBuy.setText(price_buy);
        tvPriceSell.setText(price_sell);
        setZhiSunAndZhiYing(price_buy, price_sell);

    }

    /**
     * 设置止损 止盈 的限制价格
     *
     * @param price_buy
     * @param price_sell
     */
    private synchronized void setZhiSunAndZhiYing(String price_buy, String price_sell) {
        if (price_buy == null || price_buy.equals("") || price_buy.equals("null")) {
            return;
        }
        if (price_sell == null || price_sell.equals("")) {
            return;
        }
        //买涨情况
        if (type_order == 1) {
            //止损值 = 买入价 - range
            double subtract = NumberUtils.subtract(Double.valueOf(price_buy), range);
            zhiSunValue = NumberUtils.setScale(subtract, digit);
            //止盈值 = 买入价 + range
            double add = NumberUtils.add(Double.valueOf(price_buy), range);
            zhiYingValue = NumberUtils.setScale(add, digit);
            tvLossHint.setText("   止损价格 ≤ " + zhiSunValue);
            tvProfitHint.setText("   止盈价格 ≥ " + zhiYingValue);
            //买跌情况
        } else {
            //止损值 = 卖出价 + range
            double add = NumberUtils.add(Double.valueOf(price_sell), range);
            zhiSunValue = NumberUtils.setScale(add, digit);
            //止盈值 = 卖出价 - range
            double subtract = NumberUtils.subtract(Double.valueOf(price_sell), range);
            zhiYingValue = NumberUtils.setScale(subtract, digit);
            tvLossHint.setText("   止损价格 ≥ " + zhiSunValue);
            tvProfitHint.setText("   止盈价格 ≤ " + zhiYingValue);
        }
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void closeOrderSuccess() {
        Log.i("closeOrderSuccess", "closeOrderSuccess:    下单成功了");

    }

    @Override
    public void closeOrderFailed(String errormsg) {
        Log.i("closeOrderFailed", "closeOrderSuccess:    下单失败  ：" + errormsg);
    }

}
