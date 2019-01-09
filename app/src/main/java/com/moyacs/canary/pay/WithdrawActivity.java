package com.moyacs.canary.pay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.moyacs.canary.base.BaseActivity2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.pay.tixian.BankBean;
import com.moyacs.canary.pay.tixian.BankNumberUtils;
import com.moyacs.canary.pay.tixian.GetJsonDataUtil;
import com.moyacs.canary.pay.tixian.JsonBean;
import com.moyacs.canary.pay.tixian.contract.WithdrawCountract;
import com.moyacs.canary.pay.tixian.contract.WithdrawPresenterImpl;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 提现页面
 */
public class WithdrawActivity extends BaseActivity2 implements WithdrawCountract.WithdrawView {
    @BindView(R.id.text_quanbutixian)
    TextView textQuanbutixian;
    @BindView(R.id.ed_inputMoney)
    EditText edInputMoney;
    @BindView(R.id.tv_daozhangshijian)
    TextView tvDaozhangshijian;
    @BindView(R.id.image_help)
    ImageView imageHelp;
    @BindView(R.id.ed_inputBankNumber)
    EditText edInputBankNumber;
    @BindView(R.id.ed_inputUserName)
    EditText edInputUserName;
    @BindView(R.id.tv_selectBank)
    TextView tvSelectBank;
    @BindView(R.id.tv_selectAddress)
    TextView tvSelectAddress;
    @BindView(R.id.ed_bankBranch)
    EditText edBankBranch;
    @BindView(R.id.rl_selectBank)
    RelativeLayout rlSelectBank;
    @BindView(R.id.rl_selectAddress)
    RelativeLayout rlSelectAddress;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private View rootView;
    private Unbinder unbinder;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private ArrayList<String> bank_list = new ArrayList<>();

    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static final int MSG_LOAD_BANK_SUCCESS = 0x0004;

    private boolean isLoaded = false;
    private boolean isLoadedBank = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initAddressJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    break;

                case MSG_LOAD_BANK_SUCCESS:
                    isLoadedBank = true;
                    break;
            }
        }
    };
    private WithdrawCountract.WithdrawPresenter presenter;

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_msg).setVisible(false);
    }

    @Override
    protected void initIntentData(Intent intent) {
        presenter = new WithdrawPresenterImpl(this);

        //进入页面先解析地区数据
        initAddressJsonData();
        //解析银行 json 文件
        initBankJsonData();
        //初始化一些 view 格式限制
        initViews();

    }
    //上次输入框中的内容
    private String lastString;
    //光标的位置
    private int selectPosition;


    /**
     *
     */
    private void initViews() {


       edInputBankNumber.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }


        /**
         * 当输入框内容改变时的回调
         * @param s  改变后的字符串
         * @param start 改变之后的光标下标
         * @param before 删除了多少个字符
         * @param count 添加了多少个字符
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //如果"."在起始位置,则起始位置自动补0
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                edInputBankNumber.setText(s);
                edInputBankNumber.setSelection(2);
            }

            //因为重新排序之后setText的存在
            //会导致输入框的内容从0开始输入，这里是为了避免这种情况产生一系列问题
            if (start == 0 && count > 1) {
                return;
            }
            String textTrim = BankNumberUtils.getTextTrim(edInputBankNumber);
            if (TextUtils.isEmpty(textTrim)) {
                return;
            }
            //如果before > 0,代表此次操作是删除操作
            if (before > 0) {
                selectPosition = start;
                if (TextUtils.isEmpty(lastString)) {
                    return;
                }
                //将上次的字符串去空格 和 改变之后的字符串去空格 进行比较
                //如果一致，代表本次操作删除的是空格
                if (textTrim.equals(lastString.replaceAll(" ", ""))) {
                    //帮助用户删除该删除的字符，而不是空格
                    StringBuffer stringBuffer = new StringBuffer(lastString);
                    stringBuffer.deleteCharAt(start - 1);
                    selectPosition = start - 1;
                    edInputBankNumber.setText(stringBuffer.toString());
                }
            } else {
                //此处代表是添加操作
                //当光标位于空格之前，添加字符时，需要让光标跳过空格，再按照之前的逻辑计算光标位置
                //第一次空格出现的位置是4，第二次是4+1(空格)+4=9，第三次是4+1(空格)+4+1(空格)+4=14
                //如果按照数学公式，则当start = 5n-1时，需要让光标跳过空格
                //也就是当stat%5 == 4时
                if (start % 5 == 4) {
                    selectPosition = start + count + 1;
                } else {
                    selectPosition = start + count;
                }
            }
        }


        @Override
        public void afterTextChanged(Editable s) {

            //获取输入框中的内容,不可以去空格
            String etContent = BankNumberUtils.getText(edInputBankNumber);

            //重新拼接字符串
            String newContent = BankNumberUtils.addSpeaceByCredit(etContent);
            //保存本次字符串数据
            lastString = newContent;

            //如果有改变，则重新填充
            //防止EditText无限setText()产生死循环
            if (!newContent.equals(etContent)) {
                edInputBankNumber.setText(newContent);
                //保证光标的位置
                edInputBankNumber.setSelection(selectPosition > newContent.length() ? newContent.length() : selectPosition);
            }


        }
    });
    }


    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_withdraw, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 是否显示左侧返回键
     *
     * @return
     */
    @Override
    protected Boolean isShowBack() {
        return true;
    }

    @Override
    protected String setToolbarTitle(String title) {
        return "提现";
    }

    @OnClick({R.id.text_quanbutixian, R.id.image_help, R.id.rl_selectBank, R.id.rl_selectAddress, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_quanbutixian://全部提现
                break;
            case R.id.image_help://问号按钮
                showPopopWindow_help(view);
                break;
            case R.id.btn_submit:
                submit();
                break;

            case R.id.rl_selectBank://选择银行

                //条件选择器
                OptionsPickerView pvOptions2 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = bank_list.get(options1);

                        tvSelectBank.setText(tx);

                    }
                })
                        .setTitleText("银行选择")
                        .setDividerColor(Color.BLACK)
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

                pvOptions2.setPicker(bank_list);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
//                pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
                pvOptions2.show();

                break;
            case R.id.rl_selectAddress://选择地区
                if (!isLoaded) {
                    return;
                }
                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = options1Items.get(options1).getPickerViewText() + "  " +
                                options2Items.get(options1).get(options2) + "  " +
                                options3Items.get(options1).get(options2).get(options3);
                        tvSelectAddress.setText(tx);


                    }
                })

                        .setTitleText("地区选择")
                        .setDividerColor(Color.BLACK)
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

//        pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
                pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
                pvOptions.show();
                break;
        }
    }

    /**
     * 问号按钮的 pop
     * @param view
     */
    private void showPopopWindow_help(View view) {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_tixian_help, null, false);
        PopupWindow popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_common_selected)));
        popupWindow.showAsDropDown(view);
    }

    /**
     * 提交提现请求
     */
    private void submit() {
        //mt4id
        int mt4id = SPUtils.getInstance().getInt(AppConstans.mt4id);
        //type
        String type = "WireTransfer";
        //金额
        String amount2 = edInputMoney.getText().toString().trim();
        double amount;
        if (amount2.equals("") || amount2.endsWith(".")) {
            amount = 0;
        }else {
            amount = Double.valueOf(amount2);
        }
        //账号
        String accountNumber = edInputBankNumber.getText().toString().trim();
        //账户名  用户名
        String accountName = edInputUserName.getText().toString().trim();
        //发卡行
        String bankName = tvSelectBank.getText().toString().trim();
        //支行
        String bankAddress = edBankBranch.getText().toString().trim();
        Log.i("WithdrawActivity", "mt4id:" + mt4id + "\n"
                + "type:" + type + "\n"
                + "amount:" + amount + "\n"
                + "accountNumber:" + accountNumber + "\n"
                + "accountName:" + accountName + "\n"
                + "bankName:" + bankName + "\n"
                + "bankAddress:" + bankAddress + "\n"

        );
        presenter.withdraw(mt4id,type,amount,accountNumber,accountName,bankName,bankAddress);

    }

    /**
     * //解析数据
     */
    private void initAddressJsonData() {

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    /**
     * 解析地区数据
     *
     * @param result
     * @return
     */
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    /**
     * 解析银行数据
     */
    private void initBankJsonData() {
        String jsonData = new GetJsonDataUtil().getJson(this, "bank.json");//获取assets目录下的json文件数据
        bank_list = parseBankData(jsonData);
        mHandler.sendEmptyMessage(MSG_LOAD_BANK_SUCCESS);

    }

    /**
     * 即系银行数据
     *
     * @param result
     * @return
     */
    public ArrayList<String> parseBankData(String result) {//Gson 解析
        ArrayList<String> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {

                BankBean entity = gson.fromJson(data.optJSONObject(i).toString(), BankBean.class);
                String text = entity.getText();
                detail.add(text);
                Log.i("parseBankData", "parseBankData: " + entity.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        unbinder.unbind();
    }


    @Override
    public void unsubscribe() {
        presenter.unsubscribe();

    }

    @Override
    public void showLoadingDialog() {
        startLoading();

    }

    @Override
    public void dismissLoadingDialog() {
        stopLoading();
    }

    @Override
    public void withdrawSucess(Object result) {
        Log.i("WithdrawActivity", "withdrawSucess: ");
        LemonHello.getSuccessHello("提交成功", "")
                .setContentFontSize(14)
                //取消图标
                .setIconWidth(0)
                .addAction(new LemonHelloAction("我知道啦", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                    }
                }))
               .show(this);

    }

    @Override
    public void withdrawFailed(String errormsg) {
        Log.i("WithdrawActivity", "withdrawFailed: ");
        LemonHello.getSuccessHello("提交失败", "")
                .setContentFontSize(14)
                //取消图标
                .setIconWidth(0)
                .addAction(new LemonHelloAction("我知道啦", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                    }
                }))
                .show(this);
    }
}
