package com.moyacs.canary.moudle.me.uploadidcard;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.UserInformBean;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.util.ChoicePhotoManger;
import com.moyacs.canary.util.FileUtil;
import com.moyacs.canary.util.IPermissionsResultListener;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.widget.UpdateHeadPortraitDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

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
public class UpLoadIdCardActivity extends BaseActivity implements UpLoadIdCardContract.View {
    @BindView(R.id.iv_break)
    ImageView ivBreak;
    @BindView(R.id.real_name_auth_top_rl)
    RelativeLayout realNameAuthTopRl;
    @BindView(R.id.idcard_positive_iv)
    ImageView idcardPositiveIv;
    @BindView(R.id.idcard_add_positive_iv)
    ImageView idcardAddPositiveIv;
    @BindView(R.id.idcard_reverse)
    ImageView idcardReverse;
    @BindView(R.id.idcard_add_reverse_iv)
    ImageView idcardAddReverseIv;
    @BindView(R.id.real_name_auth_before_btn)
    Button realNameAuthBeforeBtn;
    @BindView(R.id.real_name_auth_after_btn)
    Button realNameAuthAfterBtn;


    private UpLoadIdCardContract.Presenter mPresenter;
    private UpdateHeadPortraitDialog portraitDialog;
    private ChoicePhotoManger photoManger;
    private boolean isBefor =false;
    private boolean isAfter =false;
    private UserInformBean mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_uploadidcard;
    }

    @Override
    protected void initView() {
        Intent mIntent = getIntent();
        if(mIntent!=null){
            mBean = (UserInformBean) mIntent.getSerializableExtra("USER_INFORM");
        }
        new UpLoadCardPresenter(this);
        photoManger = new ChoicePhotoManger();
        portraitDialog = new UpdateHeadPortraitDialog(this);
    }

    @Override
    protected void intListener() {
        portraitDialog.setItemClickListen(new UpdateHeadPortraitDialog.ItemClickListen() {
            @Override
            public void cameraClickListen() {
                requestPermission(new String[]{Manifest.permission.CAMERA}, new IPermissionsResultListener() {
                    @Override
                    public void onSuccess() {
                        try {
                            photoManger.takePhoto(UpLoadIdCardActivity.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure() {
                        showMsg("权限不足，无法进行拍照");
                    }
                });
            }

            @Override
            public void albumClickListen() {
                requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new IPermissionsResultListener() {
                    @Override
                    public void onSuccess() {
                        photoManger.takeAlbum(UpLoadIdCardActivity.this);
                    }

                    @Override
                    public void onFailure() {
                        showMsg("权限不足，无法进行选择图片");
                    }
                });
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri newUri;
            if (requestCode == ChoicePhotoManger.TACK_ALBUM_CODE && data != null) {

                if (data != null) {
                    String realPathFromUri = FileUtil.getRealPathFromUri(this, data.getData());
                    File file = FileUtil.getSmallBitmap(this, realPathFromUri);
                    if(file!=null){
                        if(mPresenter!=null){
                            mPresenter.uploadIdCard(UpLoadIdCardActivity.this,file,isBefor,isAfter);
                        }
                    }else {
                        showMsg("选择图片地址异常!");
                    }
                } else {
                    ToastUtils.showShort("图片损坏，请重新选择");
                }

//                //相册选择后回调
//                if (data.getData() != null) {
//                    newUri =FileUtil.getImageUri(this,data);
// //                   newUri = Uri.parse(data.getData().toString());
//                    if(newUri!=null){
//                       File file = FileUtil.getSmallBitmap(this, newUri.getPath());
//                        if(file!=null){
//                            if(mPresenter!=null){
//                                mPresenter.uploadIdCard(UpLoadIdCardActivity.this,file,isBefor,isAfter);
//                            }
//                        }else {
//                            showMsg("选择图片地址异常!");
//                        }
//                    }else {
//                        showMsg("选择图片地址异常!");
//                    }
//                } else {
//                    showMsg("选择图片地址异常");
//                }

            } else if (requestCode == ChoicePhotoManger.TACK_CAMERA_CODE) {
                //相机拍照后回调
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    newUri = FileProvider.getUriForFile(this,"com.moyacs.canary.FileProvider", photoManger.getTakeFile());
                } else {
                    newUri = Uri.fromFile(photoManger.getTakeFile());
                }
                if(newUri!=null){
                    File file = FileUtil.getSmallBitmap(this, photoManger.getTakeFile().getPath());
                    if(file!=null){
                        if(mPresenter!=null){
                            mPresenter.uploadIdCard(UpLoadIdCardActivity.this,file,isBefor,isAfter);
                        }
                    }else {
                        showMsg("拍照异常!");
                    }
                }else {
                    showMsg("拍照异常!");
                }
            }
        } else {
            if (requestCode == ChoicePhotoManger.TACK_CAMERA_CODE || requestCode == ChoicePhotoManger.TACK_ALBUM_CODE) {
                FileUtil.deleteFile(photoManger.getTakeFile());
            }
        }

    }



    @Override
    protected void initData() {

        if(mBean!=null&&!TextUtils.isEmpty(mBean.getIdcard_afterPath())){
            if(idcardAddPositiveIv!=null){
                idcardAddPositiveIv.setVisibility(View.GONE);
            }
            Glide.with(UpLoadIdCardActivity.this)
                    .load(mBean.getIdcard_afterPath())
                    .into(idcardPositiveIv);
        }

        if(mBean!=null&&!TextUtils.isEmpty(mBean.getIdcard_beforPath())){
            if(idcardAddReverseIv!=null){
                idcardAddReverseIv.setVisibility(View.GONE);
            }
            Glide.with(UpLoadIdCardActivity.this)
                    .load(mBean.getIdcard_beforPath())
                    .into(idcardReverse);
        }

        if(mBean!=null&&!TextUtils.isEmpty(mBean.getIdcard_afterPath())&&!TextUtils.isEmpty(mBean.getIdcard_beforPath())){
            realNameAuthAfterBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public void showNextResult(boolean state) {
        if(state){
            ToastUtils.showShort("认证成功!");
            //刷新我的页面
            EventBus.getDefault().post(new EvenVo(EvenVo.EVENT_CODE_UPDATE_NICK_NAME));
            this.finish();
        }
    }

    @Override
    public void uploadBeforImgSuccess(String imgPath) {
        if(mBean!=null){
            mBean.setIdcard_beforPath(imgPath);
        }
        if(idcardAddPositiveIv!=null){
            idcardAddPositiveIv.setVisibility(View.GONE);
        }
        Glide.with(UpLoadIdCardActivity.this)
                .load(imgPath)
                .into(idcardPositiveIv);

    }

    @Override
    public void uploadAfterImgSuccess(String imgPath) {
        if(mBean!=null){
            mBean.setIdcard_afterPath(imgPath);
        }
        if(idcardAddReverseIv!=null){
            idcardAddReverseIv.setVisibility(View.GONE);
        }
        Glide.with(UpLoadIdCardActivity.this)
                .load(imgPath)
                .into(idcardReverse);

    }

    @Override
    public void setPresenter(UpLoadIdCardContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtils.showShort(msg);
    }

    @OnClick({R.id.idcard_add_positive_iv, R.id.idcard_add_reverse_iv, R.id.real_name_auth_before_btn, R.id.real_name_auth_after_btn,R.id.iv_break})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idcard_add_positive_iv:
                isBefor =true;
                isAfter=false;
                portraitDialog.show();
                break;
            case R.id.idcard_add_reverse_iv:
                isBefor =false;
                isAfter=true;
                portraitDialog.show();
                break;
            case R.id.real_name_auth_before_btn:
                onBackPressed();
                break;
            case R.id.real_name_auth_after_btn:
                if(mPresenter!=null&&mBean!=null){
                    mPresenter.submitRealNameInform(mBean.getSex(),mBean.getAge(),mBean.getIdcard(),mBean.getIdcard_beforPath(),mBean.getIdcard_afterPath(),mBean.getName());
                }
                break;
            case R.id.iv_break:
                onBackPressed();
                break;
        }
    }
}
