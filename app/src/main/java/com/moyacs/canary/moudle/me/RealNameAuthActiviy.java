package com.moyacs.canary.moudle.me;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.UserInformBean;
import com.moyacs.canary.moudle.me.uploadidcard.UpLoadIdCardActivity;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.TimeUtils;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.widget.CommonAlertDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/21
 * @email 252774645@qq.com
 */
public class RealNameAuthActiviy extends BaseActivity implements RealNameAuthContract.View {


    @BindView(R.id.real_name_auth_phone_et)
    EditText realNameAuthPhoneEt;
    @BindView(R.id.real_name_auth_name_et)
    EditText realNameAuthNameEt;
    @BindView(R.id.real_name_auth_sex_et)
    TextView realNameAuthSexEt;
    @BindView(R.id.real_name_auth_age_et)
    TextView realNameAuthAgeEt;
//    @BindView(R.id.real_name_auth_address_et)
//    EditText realNameAuthAddressEt;
    @BindView(R.id.real_name_auth_idcard_et)
    EditText realNameAuthIdcardEt;
    @BindView(R.id.real_name_auth_cb1)
    CheckBox realNameAuthCb1;
    @BindView(R.id.real_name_auth_cb_title)
    TextView realNameAuthCbTitle;
    @BindView(R.id.real_name_auth_cb_rl)
    RelativeLayout realNameAuthCbRl;
    @BindView(R.id.real_name_auth_notice_title)
    TextView realNameAuthNoticeTitle;
    @BindView(R.id.real_name_auth_cb2)
    CheckBox realNameAuthCb2;
    @BindView(R.id.real_name_auth_cb2_title)
    TextView realNameAuthCb2Title;
    @BindView(R.id.real_name_auth_cb3)
    CheckBox realNameAuthCb3;
    @BindView(R.id.real_name_auth_cb3_title)
    TextView realNameAuthCb3Title;
    @BindView(R.id.real_name_auth_phone_ll)
    LinearLayout realNameAuthPhoneLl;
    @BindView(R.id.real_name_auth_next_title_btn)
    Button realNameAuthNextTitleBtn;
    @BindView(R.id.iv_break)
    ImageView backIv;

    private RealNameAuthContract.Presenter mPresenter;
    private String mPhone;
    private UserInformBean mBean;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_realnameauth;
    }

    @Override
    protected void initView() {
            Intent mIntent = getIntent();
            if(mIntent!=null){
                mBean = (UserInformBean) mIntent.getSerializableExtra("USER_INFORM");
            }
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        mPhone =SharePreferencesUtil.getInstance().getUserPhone();
        if (!TextUtils.isEmpty(mPhone)) {
            realNameAuthPhoneEt.setText(mPhone);
            realNameAuthPhoneEt.setEnabled(false);
        }
        realNameAuthCb1.setChecked(true);
        realNameAuthCb2.setChecked(true);
    }



    @Override
    public void setPresenter(RealNameAuthContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtils.showShort(msg);
    }



    @OnClick({R.id.real_name_auth_phone_et, R.id.real_name_auth_name_et, R.id.real_name_auth_sex_et, R.id.real_name_auth_age_et,R.id.iv_break,R.id.real_name_auth_next_title_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.real_name_auth_phone_et:

                break;
            case R.id.real_name_auth_name_et:

                break;
            case R.id.real_name_auth_sex_et:
                //条件选择器
                List<String> mLists = new ArrayList<>();
                mLists.add("男");
                mLists.add("女");
                OptionsPickerView pvOptions2 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = mLists.get(options1);
                        if(realNameAuthSexEt!=null){
                            realNameAuthSexEt.setText(tx);
                        }
                    }
                })
                        .setTitleText("性别选择")
                        .setDividerColor(Color.BLACK)
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setCancelColor(Color.GRAY)//取消按钮文字颜色
                        .setContentTextSize(20)
                        .build();
                pvOptions2.setPicker(mLists);//一级选择器
                pvOptions2.show();
                break;
            case R.id.real_name_auth_age_et:
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                //正确设置方式 原因：注意事项有说明
                startDate.set(2013,0,1);
                endDate.set(2020,11,31);
                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        if(realNameAuthAgeEt!=null){
                            realNameAuthAgeEt.setText(TimeUtils.getTime(date));
                        }
                    }
                })
                    .setType(new boolean[]{true, true, true,false,false,false})// 默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setTitleSize(20)//标题文字大小
                        .setTitleText("出生年月")//标题文字
                        .isCyclic(true)//是否循环滚动
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setCancelColor(Color.GRAY)//取消按钮文字颜色
                        .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                        .setRangDate(startDate,endDate)//起始终止年月日设定
                        .setLabel("年","月","日","","","")//默认设置为年月日时分秒
                        .isCenterLabel(false)
                        .isDialog(true)
                        .build();
                pvTime.show();

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
                        dialogWindow.setDimAmount(0.1f);
                    }
                }


                break;

            case R.id.real_name_auth_next_title_btn:

                mBean = new UserInformBean();
                if (TextUtils.isEmpty(mPhone)) {
                    showMessageTips("电话号码不能为空!");
                    return;
                }
                mBean.setPhone(mPhone);

                String mName =realNameAuthNameEt.getText().toString();
                if (TextUtils.isEmpty(mName)) {
                    showMessageTips("真实姓名不能为空!");
                    return;
                }
                mBean.setName(mName);

                String mSex =realNameAuthSexEt.getText().toString();
                if (TextUtils.isEmpty(mSex)) {
                    showMessageTips("性别不能为空!");
                    return;
                }
                mBean.setSex(mSex);

                String mAge =realNameAuthAgeEt.getText().toString();
                if (TextUtils.isEmpty(mAge)) {
                    showMessageTips("出生日期不能为空!");
                    return;
                }
                mBean.setAge(mAge);


                String mIdCard =realNameAuthIdcardEt.getText().toString();
                if (TextUtils.isEmpty(mIdCard)) {
                    showMessageTips("身份证号不能为空!");
                    return;
                }
                mBean.setIdcard(mIdCard);

//                String mAddress =realNameAuthAddressEt.getText().toString();
//                if (TextUtils.isEmpty(mAddress)) {
//                    showMessageTips("地址不能为空!");
//                    return;
//                }
//                mBean.setAddress(mAddress);


                if(!(realNameAuthCb1!=null&&realNameAuthCb1.isChecked())){
                    showMessageTips("请选择我不是美国公民按钮!");
                    return;
                }

                if(!(realNameAuthCb2!=null&&realNameAuthCb2.isChecked())){
                    showMessageTips("请选择交易相关问题按钮!");
                    return;
                }

                if(!(realNameAuthCb3!=null&&realNameAuthCb3.isChecked())){
                    showMessageTips("请选择各项条款条约按钮!");
                    return;
                }

                CommonAlertDialog commonAlertDialog = new CommonAlertDialog(this, getString(R.string.real_name_auth_dialog), getString(R.string.common_dialog_ok), new CommonAlertDialog.ConfirmListener() {
                    @Override
                    public void callback() {
                        Intent mIntent = new Intent(RealNameAuthActiviy.this, UpLoadIdCardActivity.class);
                        mIntent.putExtra("USER_INFORM",mBean);
                        startActivity(mIntent);

                    }
                });
                commonAlertDialog.show();
                break;
            case R.id.iv_break:
                onBackPressed();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mBean!=null){
            if (!TextUtils.isEmpty(mBean.getPhone())) {
                realNameAuthPhoneEt.setText(mBean.getPhone());
            }

            if (!TextUtils.isEmpty(mBean.getName())) {
                realNameAuthNameEt.setText(mBean.getName());
            }

            if (!TextUtils.isEmpty(mBean.getSex())) {
                realNameAuthSexEt.setText(mBean.getSex());
            }

            if (!TextUtils.isEmpty(mBean.getAge())) {
                realNameAuthAgeEt.setText(mBean.getAge());
            }

            if (!TextUtils.isEmpty(mBean.getIdcard())) {
                realNameAuthIdcardEt.setText(mBean.getIdcard());
            }

            if(mBean.isAuth()){
                realNameAuthPhoneEt.setEnabled(false);
                realNameAuthNameEt.setEnabled(false);
                realNameAuthSexEt.setEnabled(false);
                realNameAuthAgeEt.setEnabled(false);
                realNameAuthPhoneEt.setEnabled(false);
                realNameAuthIdcardEt.setEnabled(false);
            }
        }
    }
}
