package com.moyacs.canary.pay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.common.StringUtil;
import com.moyacs.canary.pay.contract.PayContract;
import com.moyacs.canary.pay.contract.PayPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/4/12 0012 11:02
 * 邮箱：rsf411613593@gmail.com
 * 说明：挂单
 */

public class GuaDanFragment extends BaseFragment2 implements PayContract.PayView {

    Unbinder unbinder;
    /**
     * 买涨文字
     */
//    @BindView(R.id.textUpView)
//    TextView textUpView;
    /**
     * 买涨按钮
     */
//    @BindView(R.id.upView)
//    RelativeLayout upView;
    /**
     * 买跌文字
     */
//    @BindView(R.id.textDownView)
//    TextView textDownView;
    /**
     * 买跌按钮
     */
//    @BindView(R.id.downView)
//    RelativeLayout downView;
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
    Unbinder unbinder1;
//    @BindView(R.id.textUpView2)
//    TextView textUpView2;
//    @BindView(R.id.upView2)
//    RelativeLayout upView2;
//    @BindView(R.id.textDownView2)
//    TextView textDownView2;
//    @BindView(R.id.downView2)
//    RelativeLayout downView2;
    //    @BindView(R.id.date_left)
//    LinearLayout dateLeft;
//    @BindView(R.id.tv_date_1)
//    TextView tvDate1;
//    @BindView(R.id.rl_date1)
//    RelativeLayout rlDate1;
//    @BindView(R.id.tv_date_2)
//    TextView tvDate2;
//    @BindView(R.id.rl_date2)
//    RelativeLayout rlDate2;
    @BindView(R.id.lessnumView2)
    RelativeLayout lessnumView2;
    /**
     * 挂单价
     */
    @BindView(R.id.ed_num2)
    EditText edNum2;
    @BindView(R.id.tv_guaDanHint)
    TextView tvGuaDanHint;
    @BindView(R.id.addnumView2)
    RelativeLayout addnumView2;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rl_date)
    RelativeLayout rlDate;
    @BindView(R.id.tv_guadantype)
    TextView tvGuadantype;
    private GradientDrawable gradientDrawable;
    /**
     * 1 买涨 ， 2 买跌 3,挂单
     */
    private int type_order;
    /**
     * 挂单的 type
     */
    private String type;
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
    private int year;
    private int month;
    private int day;
    //获取当前时间
    private Calendar calendar;
    private TimePickerView pvTime;
    private View rootView;
    private String expiredDate;
    private PopupWindow pop_guadan_type;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {

        rootView = inflater.inflate(R.layout.fragment_guadan, null);
        unbinder = ButterKnife.bind(this, rootView);
        initViews();
        initEditText();
        return rootView;
    }


    /**
     * 初始化 相关view
     */
    private void initViews() {
        //初始化买涨限价背景色
//        setBg_maiZhang();
        //默认选中  买涨限价
        type = AppConstans.type_BUY_LIMIT;

        //初始化挂单价
        if (price_buy.equals("") || price_buy.equals("null")) {
            edNum2.setText("0");
        } else {
            Double aDouble = Double.valueOf(price_buy);
            double subtract = NumberUtils.subtract(aDouble, range);
            String s = NumberUtils.setScale(subtract, digit);
            edNum2.setText(s);
        }
        //手数提示信息
        tvGuaDanHint.setText("挂单价不能高于：买入价-" + stops_level + "个点");


        //初始化  止盈 止损价格的提示信息
        tvLossHint.setText("止损价格不能高于：挂单价-" + stops_level + "个点");
        tvProfitHint.setText("止盈价格不能低于：挂单价+" + stops_level + "个点");

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
        //挂单价
        edNum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edNum2.setText(s);
                    edNum2.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edNum2.setText(s.subSequence(0, 1));
                        edNum2.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edLoss.setText(s.subSequence(0, 1));
                        edLoss.setSelection(1);
                        return;
                    }
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
                if (type.equals(AppConstans.type_BUY_LIMIT) || type.equals(AppConstans.type_BUY_STOP)) {

                    if (valueOf > zhiSunValue_d) {
                        String s2 = NumberUtils.setScale(zhiSunValue_d, digit);
                        edLoss.setText(s2);

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

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edProfit.setText(s.subSequence(0, 1));
                        edProfit.setSelection(1);
                        return;
                    }
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
     * 点击买涨限价的背景色设置
     */
    private void setBg_maiZhang() {
//        upView.setSelected(true);
//        upView2.setSelected(false);
//        downView.setSelected(false);
//        downView2.setSelected(false);
//        setBtnBackground(getResources().getColor(R.color.color_opt_gt));
    }

    /**
     * 点击买涨止损的背景色设置
     */
    private void setBg_maiZhang2() {
//        upView.setSelected(false);
//        upView2.setSelected(true);
//        downView.setSelected(false);
//        downView2.setSelected(false);
//        setBtnBackground(getResources().getColor(R.color.color_opt_gt));
    }

    /**
     * 点击买跌限价时候的背景色设置
     */
    private void setBg_maiDie() {
//        downView.setSelected(true);
//        downView2.setSelected(false);
//        upView.setSelected(false);
//        upView2.setSelected(false);
//        setBtnBackground(getResources().getColor(R.color.color_opt_lt));
    }

    /**
     * 点击买跌限价时候的背景色设置
     */
    private void setBg_maiDie2() {
//        downView.setSelected(false);
//        downView2.setSelected(true);
//        upView.setSelected(false);
//        upView2.setSelected(false);
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
        Log.i("stops_level", "stops_level:  " + stops_level);
        Log.i("price_buy", "price_buy:  " + price_buy);
        Log.i("price_sell", "price_sell:  " + price_sell);
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


//    /**
//     * 设置下单按钮背景色
//     *
//     * @param color
//     */
//    private void setBtnBackground(int color) {
//        if (gradientDrawable == null) {
//
//            gradientDrawable = (GradientDrawable) btnOrder.getBackground();
//        }
//        gradientDrawable.setColor(color);
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @OnClick({ R.id.lessnumView, R.id.addnumView, R.id.lossViewLeft,
            R.id.lessLossView, R.id.addLossView, R.id.profitViewLeft, R.id.lessProfitView,
            R.id.addProfitView, R.id.tv_cashin, R.id.btn_order,
            R.id.lessnumView2, R.id.addnumView2, R.id.rl_guadan_type,
            R.id.rl_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_guadan_type://选择挂单
                showPop(tvGuadantype);
                break;

            case R.id.lessnumView2://挂单价 减

                String trim1 = edNum2.getText().toString().trim();
                if (trim1 == null || trim1.equals("")) {
                    if (type.equals(AppConstans.type_BUY_LIMIT) || type.equals(AppConstans.type_BUY_STOP)) {

                        edNum2.setText(price_buy);
                    } else {
                        edNum2.setText(price_sell);
                    }
                    edNum2.setSelection(edNum2.getText().toString().trim().length());
                    return;
                }
                Double trim1_d = Double.valueOf(trim1);
                double subtract1 = NumberUtils.subtract(trim1_d, range_change);
                String s3 = NumberUtils.setScale(subtract1, digit);
                edNum2.setText(s3);
                edNum2.setSelection(edNum2.getText().toString().trim().length());

                break;

            case R.id.addnumView2://挂单价 加
                String trim2 = edNum2.getText().toString().trim();
                if (trim2 == null || trim2.equals("")) {
                    if (type.equals(AppConstans.type_BUY_LIMIT) || type.equals(AppConstans.type_BUY_STOP)) {

                        edNum2.setText(price_buy);
                    } else {
                        edNum2.setText(price_sell);
                    }
                    edNum2.setSelection(edNum2.getText().toString().trim().length());
                    return;
                }
                Double trim2_d = Double.valueOf(trim2);
                double add = NumberUtils.add(trim2_d, range_change);
                String s4 = NumberUtils.setScale(add, digit);
                edNum2.setText(s4);
                edNum2.setSelection(edNum2.getText().toString().trim().length());
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

            case R.id.rl_date://选择日期
                showDateToSelected();
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
                    edLoss.setText(getZhiSunValue());
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
                    edProfit.setText(getZhiYingValue());

                }
                break;
            case R.id.lessProfitView://止盈 减
                setProfitEditText(false);
                break;
            case R.id.addProfitView:// 止盈 加
                setProfitEditText(true);
                break;

//            case R.id.date_left://日期左侧布局
//                break;
//            case R.id.rl_date1://选择日期
//                showDateDialog();
//                break;
//            case R.id.rl_date2://选择时间
//                showTimeDialog();
//                break;
            case R.id.tv_cashin://充值
                long nowMills = TimeUtils.getNowMills();
                String url = "http://uc.moyacs.com/my.account-deposit.funds_app_v2.html?v=" + nowMills + "&mt4id=812999&token=xxxxxxx";
                Intent intent = new Intent(getContext(), PayActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.btn_order://下单
                // MT4 ID
                int mt4id = SPUtils.getInstance().getInt(AppConstans.mt4id);
                //server
                String server = SPUtils.getInstance().getString(AppConstans.type);


                String trim = edNum.getText().toString().trim();
                trim = StringUtil.endWithPoint(trim);
                Double volume = Double.valueOf(trim);
                if (volume <= 0) {
                    volume = 0.01;
                }
                //手数
                Double multiply = NumberUtils.multiply(volume, 100);

                //止损价
                double sl;
                if (lossViewLeft.isSelected()) {
                    String trim_loss = edLoss.getText().toString().trim();
                    if (trim_loss.equals("")) {
                        sl = 0;
                    } else {
                        trim_loss = StringUtil.endWithPoint(trim_loss);
                        Double aDouble = Double.valueOf(trim_loss);
                        String s1 = NumberUtils.setScale(aDouble, digit);
                        sl = Double.valueOf(s1);

                    }
                } else {
                    sl = 0D;
                }
                //止盈价格
                double tp;
                if (profitViewLeft.isSelected()) {
                    String trim1_profit = edProfit.getText().toString().trim();
                    if (trim1_profit.equals("")) {
                        tp = 0;
                    } else {
                        trim1_profit = StringUtil.endWithPoint(trim1_profit);
                        Double aDouble = Double.valueOf(trim1_profit);
                        String s1 = NumberUtils.setScale(aDouble, digit);
                        tp = Double.valueOf(s1);

                    }
                } else {
                    tp = 0D;
                }

                //挂单价
                double price;
                String trim3 = edNum2.getText().toString().trim();
                if (trim3.equals("")) {
                    price = 0D;
                } else {
                    trim3 = StringUtil.endWithPoint(trim3);
                    Double aDouble = Double.valueOf(trim3);
                    String s1 = NumberUtils.setScale(aDouble, digit);
                    price = Double.valueOf(s1);
                }
                Log.i("closeOrder", "server: " + server + "\n" +
                        "mt4id: " + mt4id + "\n" +
                        "symbol: " + symbol + "\n" +
                        "type: " + type + "\n" +
                        "multiply.intValue(): " + multiply.intValue() + "\n" +
                        "sl: " + sl + "\n" +
                        "tp: " + tp + "\n" +
                        "price: " + price + "\n" +
                        "expiredDate: " + expiredDate + "\n"
                );
                PayPresenterImpl payPresenter = new PayPresenterImpl(this);
//                payPresenter.closeOrder(server, mt4id, symbol, type, multiply.intValue(), sl, tp, "", price, expiredDate);
                Log.i("mt4id", "mt4id: " + mt4id + "      server : " + server);
                break;
        }
    }

    /**
     * 日期选择器
     */
    private void showDateToSelected() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2018, 0, 1);
        endDate.set(2100, 11, 31);

        //选中事件回调
        pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvDate.setText(TimeUtils.date2String(date, new SimpleDateFormat("yyyy-MM-dd HH:mm" + ":00")));
                //提交订单时，所需要的时间
                expiredDate = TimeUtils.date2String(date, new SimpleDateFormat("dd-MM-yyyy HH:mm" + ":00"));
            }
        })
                .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择日期")//标题文字
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setTitleColor(getResources().getColor(R.color.app_common_content))//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.dialog_btn_blue))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.dialog_btn_blue))//取消按钮文字颜色
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)
                .build();
        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
        pvTime.show(rootView);
    }

    /**
     * 计算止损价格
     */
    private String getZhiSunValue() {
        String s1 = edNum2.getText().toString();
        if (s1.equals("") || s1.equals("null")) {
            return "0.0";
        } else {
            s1 = StringUtil.endWithPoint(s1);
            Double aDouble = Double.valueOf(s1);
            double add1 = NumberUtils.subtract(aDouble, range);
            String s5 = NumberUtils.setScale(add1, digit);
            return s5;
        }
    }

    /**
     * 计算止损价格
     */
    private String getZhiYingValue() {
        String s1 = edNum2.getText().toString();
        if (s1.equals("") || s1.equals("null")) {
            return s1;
        } else {
            s1 = StringUtil.endWithPoint(s1);
            Double aDouble = Double.valueOf(s1);
            double add1 = NumberUtils.add(aDouble, range);
            String s5 = NumberUtils.setScale(add1, digit);
            return s5;
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
            edProfit.setText(getZhiSunValue());
            trim = getZhiSunValue();
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
            edProfit.setText(getZhiYingValue());
            trim = getZhiYingValue();
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

        //重新设置 挂单价
//        if (type.equals(AppConstans.type_BUY_LIMIT)) {
//
//                edNum2.setText(price_buy);
//                edNum2.setSelection(edNum2.getText().toString().trim().length());
//        } else if(type.equals(AppConstans.type_BUY_STOP)) {
//
//                edNum2.setText(price_sell);
//                edNum2.setSelection(edNum2.getText().toString().trim().length());
//        }else if (type.equals(AppConstans.type_SELL_STOP)){
//
//        }else {
//
//        }


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

    private void showPop(View view) {
        //pop 布局
        View popView = View.inflate(getContext(), R.layout.pop_guadan, null);
        initPopViews(popView);

        //初始化 view 相关数据 和 参数
        pop_guadan_type = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置背景色
        pop_guadan_type.setBackgroundDrawable(new ColorDrawable(0));
        //动画效果
        pop_guadan_type.setAnimationStyle(R.style.popwin_anim_style);
        //位置
        pop_guadan_type.showAsDropDown(view);
        //设置背景半透明
        backgroundAlpha(0.6f);
        //点击空白位置，popupwindow消失的事件监听，这时候让背景恢复正常
        pop_guadan_type.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    /**
     * 初始化 pop 相关
     * @param popView
     */
    private void initPopViews(final View popView) {
        View upView = popView.findViewById(R.id.upView);
        View upView2 = popView.findViewById(R.id.upView2);
        View downView = popView.findViewById(R.id.downView);
        View downView2 = popView.findViewById(R.id.downView2);
        /**
         * 买涨限价点击事件
         */
        upView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGuadantype.setText("买涨限价");

                type = AppConstans.type_BUY_LIMIT;
                //设置买涨按钮背景
//                setBg_maiZhang();

                OnZhangOrDieClick();
                //手数提示信息
                tvGuaDanHint.setText("挂单价不能高于：买入价-" + stops_level + "个点");
                pop_guadan_type.dismiss();
            }
        });
        /**
         * 买涨止损点击事件
         */
        upView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGuadantype.setText("买涨止损");

                type = AppConstans.type_BUY_STOP;

//                setBg_maiZhang2();
                OnZhangOrDieClick();
                //手数提示信息
                tvGuaDanHint.setText("挂单价不能低于：买入价+" + stops_level + "个点");
                pop_guadan_type.dismiss();
            }
        });
        /**
         * 买跌限价点击事件
         */
        downView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGuadantype.setText("买跌限价");

                type = AppConstans.type_SELL_LIMIT;

//                setBg_maiDie();
                OnZhangOrDieClick();
                //手数提示信息
                tvGuaDanHint.setText("挂单价不能低于：卖出价+" + stops_level + "个点");
                pop_guadan_type.dismiss();
            }
        });
        /**
         * 买跌止损点击事件
         */
        downView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGuadantype.setText("买跌止损");

                type = AppConstans.type_SELL_STOP;
//                setBg_maiDie2();
                OnZhangOrDieClick();
                //手数提示信息
                tvGuaDanHint.setText("挂单价不能高于：卖出价-" + stops_level + "个点");
                pop_guadan_type.dismiss();

            }
        });
    }

    /**
     * 设置显示 popwindow 时屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }



}
