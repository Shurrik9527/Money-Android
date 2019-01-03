package com.moyacs.canary.pay;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.widget.UnderLineTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/4/12 0012 11:02
 * 邮箱：rsf411613593@gmail.com
 * 说明：市价单
 */

public class ShiJiaDanFragment extends BaseFragment2 {
    @BindView(R.id.tab1)
    UnderLineTextView tab1;
    @BindView(R.id.tab2)
    UnderLineTextView tab2;
    Unbinder unbinder;
    @BindView(R.id.tv_zhisunValue)
    TextView tvZhisunValue;
    @BindView(R.id.tv_zhisunMoney)
    TextView tvZhisunMoney;
    @BindView(R.id.btnZhisunLess)
    Button btnZhisunLess;
    @BindView(R.id.seekBarZhisun)
    SeekBar seekBarZhisun;
    @BindView(R.id.tv_maxZhisun)
    TextView tvMaxZhisun;
    @BindView(R.id.btnZhisunAdd)
    Button btnZhisunAdd;
    @BindView(R.id.tv_zhiyingValue)
    TextView tvZhiyingValue;
    @BindView(R.id.tv_zhiyingMoney)
    TextView tvZhiyingMoney;
    @BindView(R.id.btnZhiyingLess)
    Button btnZhiyingLess;
    @BindView(R.id.seekBarZhiying)
    SeekBar seekBarZhiying;
    @BindView(R.id.tv_maxZhiying)
    TextView tvMaxZhiying;
    @BindView(R.id.btnZhiyingAdd)
    Button btnZhiyingAdd;
    //    @BindView(R.id.tv_shoushuValue)
//    TextView tvShoushuValue;
    @BindView(R.id.btnShouShuLess)
    Button btnShouShuLess;
    @BindView(R.id.seekBarShoushu)
    SeekBar seekBarShoushu;
    @BindView(R.id.tv_maxShoushu)
    TextView tvMaxShoushu;
    @BindView(R.id.btnShouShuAdd)
    Button btnShouShuAdd;
    @BindView(R.id.edit_shoushu)
    EditText editShouShu;
    @BindView(R.id.btn_order)
    Button btnOrder;
    private GradientDrawable gradientDrawable;

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {

        View rootView = inflater.inflate(R.layout.fragment_shijiadan, null);
        unbinder = ButterKnife.bind(this, rootView);
        initTabList();
        setTabSelect(0);
        setBtnBackground(getResources().getColor(R.color.color_opt_gt));
        initSeekbar();
        initEditText();
        return rootView;
    }


    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected void loadData() {

    }

    /**
     * 顶部 tab 集合
     */
    private ArrayList<UnderLineTextView> tabList;

    /**
     * 将所有 tab 放入集合，方便以后状态改变
     */
    private void initTabList() {
        tabList = new ArrayList<>();
        tabList.add(tab1);
        tabList.add(tab2);

    }

    /**
     * 抽取方法，便于设置 对应tab 的选中状态
     */
    private void setTabSelect(int tabSelect) {
        int size = tabList.size();
        if (tabSelect > size || tabSelect < 0) {
            return;
        }
        UnderLineTextView underLineTextView1 = tabList.get(tabSelect);
        if (tabSelect == 0) {
            underLineTextView1.setTextColor(getResources().getColor(R.color.color_opt_gt));
        } else {
            underLineTextView1.setTextColor(getResources().getColor(R.color.green));
        }

        for (int i = 0; i < tabList.size(); i++) {
            UnderLineTextView underLineTextView = tabList.get(i);
            if (i == tabSelect) {
                underLineTextView.setSelected(true);
            } else {
                underLineTextView.setSelected(false);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tab1, R.id.tab2, R.id.btnZhisunLess, R.id.btnZhisunAdd, R.id.btnZhiyingLess,
            R.id.btnZhiyingAdd, R.id.btnShouShuLess, R.id.btnShouShuAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                setTabSelect(0);
                tab2.setTextColor(getResources().getColor(R.color.normal_grey_selected_white));
                setBtnBackground(getResources().getColor(R.color.color_opt_gt));

                break;
            case R.id.tab2:
                setTabSelect(1);
                tab1.setTextColor(getResources().getColor(R.color.normal_grey_selected_white));
                setBtnBackground(getResources().getColor(R.color.green));
                break;

            case R.id.btnShouShuLess://手数左侧减号
                int progress1 = seekBarShoushu.getProgress();
                Log.i("seekBarShoushu", "progress:    "+progress1);
                if (progress1 <= 1) {
                    return;
                }
                seekBarShoushu.setProgress(progress1 - 1);
                break;
            case R.id.btnShouShuAdd://手数右侧加号
                int progress = seekBarShoushu.getProgress();
                Log.i("seekBarShoushu", "progress:    "+progress);
                if (progress >= 2000) {
                    return;
                }
                seekBarShoushu.setProgress(progress + 1);
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
        }
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

    /**
     * 初始化 editText 监听
     */
    private void initEditText() {

        editShouShu.setText(0.01 + "");
        editShouShu.addTextChangedListener(new TextWatcher() {
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
                        editShouShu.setText(s);
                        editShouShu.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editShouShu.setText(s);
                    editShouShu.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editShouShu.setText(s.subSequence(0, 1));
                        editShouShu.setSelection(1);
                        return;
                    }
                }

                String s1 = s.toString();
                if (s1 == null || s1.equals("")) {
                    seekBarShoushu.setProgress(1);
                    return;
                }

                Double valueOf = Double.valueOf(s1);
                if (valueOf > 20) {
                    seekBarShoushu.setProgress(2000);
                    editShouShu.setText("20.00");
                    editShouShu.clearFocus();

                }


                double multiply = NumberUtils.multiply(valueOf, (double) 100);
                if (seekBarShoushu != null) {
                    seekBarShoushu.setProgress((int) multiply);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
     * 初始化 止盈 止损的 seekbar
     */

    private void initSeekbar() {
        //手数  seekbar
        seekBarShoushu.setMax(2000);
        seekBarShoushu.setProgress(1);
        seekBarShoushu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private String shouShu;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1 || progress > 2000) {
                    return;
                }
                if (editShouShu.hasFocus()) {
                    return;
                }
                double divide = NumberUtils.divide((double) progress, 100);
                //保留两位小数，不足的用 0 表示
                String scale = NumberUtils.setScale(divide, 2);

                editShouShu.setText(scale);
                editShouShu.setSelection(editShouShu.getText().toString().trim().length());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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
                int i = 10 * 1;

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
                int i = 10 * 1;
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

}
