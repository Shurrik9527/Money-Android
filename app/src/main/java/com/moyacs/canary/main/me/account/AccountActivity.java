package com.moyacs.canary.main.me.account;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.login.ForgetPasswordActivity;
import com.moyacs.canary.main.me.SetNickNameActivity;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.ChoicePhotoManger;
import com.moyacs.canary.util.FileUtil;
import com.moyacs.canary.util.IPermissionsResultListener;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.widget.CircleImageView;
import com.moyacs.canary.widget.UpdateHeadPortraitDialog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import www.moyacs.com.myapplication.R;

public class AccountActivity extends BaseActivity {

    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;

    private ChoicePhotoManger photoManger;
    private UpdateHeadPortraitDialog portraitDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initView() {
        photoManger = new ChoicePhotoManger();
        portraitDialog = new UpdateHeadPortraitDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(tvNickName!=null){
            tvNickName.setText(SharePreferencesUtil.getInstance().getNickName()+"");
        }
        if(tvAccount!=null){
            tvAccount.setText(SharePreferencesUtil.getInstance().getUserPhone()+"");
        }
    }

    @Override
    protected void intListener() {
        portraitDialog.setItemClickListen(new UpdateHeadPortraitDialog.ItemClickListen() {
            @Override
            public void cameraClickListen() {
                requestPermission(new String[]{Manifest.permission.CAMERA}, new IPermissionsResultListener() {
                    @Override
                    public void onSuccess() {
                        photoManger.takeCamera(AccountActivity.this);
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
                        photoManger.takeAlbum(AccountActivity.this);
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
    protected void initData() {
        registerEventBus();
    }

    @OnClick({R.id.iv_back, R.id.ll_nickName, R.id.ll_author, R.id.ll_updatePsw, R.id.b_outLogin, R.id.ll_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_nickName:
                //修改昵称
                startActivity(new Intent(this, SetNickNameActivity.class));
                break;
            case R.id.ll_author:
                //实名认证
                ToastUtils.showShort("正在开发中...");
                break;
            case R.id.ll_updatePsw:
                //修改密码
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.b_outLogin:
                //退出登录
                DialogUtils.login_please("确定要退出吗？", this);
                break;
            case R.id.ll_head:
                //修改头像
                portraitDialog.show();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(EvenVo vo) {
        if (vo.getCode() == EvenVo.EVENT_CODE_UPDATE_NICK_NAME) {
            if(tvNickName!=null){
                tvNickName.setText(SharePreferencesUtil.getInstance().getNickName());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri newUri;
            if (requestCode == ChoicePhotoManger.TACK_ALBUM_CODE && data != null) {
                //相册选择后回调
                if (data.getData() != null) {
                    newUri = Uri.parse(data.getData().toString());
                    photoManger.cropImageUri(this, newUri);
                } else {
                    showMsg("选择图片地址异常");
                }
            } else if (requestCode == ChoicePhotoManger.TACK_CAMERA_CODE) {
                //相机拍照后回调
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    newUri = FileProvider.getUriForFile(this,"com.moyacs.canary.FileProvider", photoManger.getTakeFile());
                } else {
                    newUri = Uri.fromFile(photoManger.getTakeFile());
                }
                photoManger.cropImageUri(this, newUri);
            } else if (requestCode == ChoicePhotoManger.CROP_IMAGE && data != null) {
                //剪切后的图片
                String cropFilePath = photoManger.getCropFile().getPath();
                if (TextUtils.isEmpty(cropFilePath)) {
                    showMsg("截切地址获取异常");
                    return;
                }
                File file = FileUtil.getSmallBitmap(this, cropFilePath);
                uploadUserImage(file); //上传头像
            }
        } else {
            if (requestCode == ChoicePhotoManger.TACK_CAMERA_CODE || requestCode == ChoicePhotoManger.TACK_ALBUM_CODE) {
                FileUtil.deleteFile(photoManger.getTakeFile());
            }
        }

    }

    /**
     * 上传头像
     *
     * @param file 头像文件地址
     */
    private void uploadUserImage(File file) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("typeName", AppConstans.toRequestBody("head"));
        List<MultipartBody.Part> bodyList = new ArrayList<>();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
        bodyList.add(body);
        addSubscribe(ServerManger.getInstance().getServer().uploadFile(map, bodyList)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        String imgHead = data.getData();
                        addSubscribe(ServerManger.getInstance().getServer().updateNickName("", imgHead)
                                .compose(RxUtils.rxSchedulerHelper())
                                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                                    @Override
                                    protected void requestSuccess(ServerResult<String> data) {
                                        ToastUtils.showShort("头像修改成功");
                                        finish();
                                        Glide.with(AccountActivity.this)
                                                .load(imgHead)
                                                .into(ivHead);
                                        SharePreferencesUtil.getInstance().setUserHead(imgHead);
                                        EventBus.getDefault().post(new EvenVo(EvenVo.EVENT_CODE_UPDATE_NICK_NAME));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        ToastUtils.showShort("头像修改失败");
                                    }

                                    @Override
                                    public void onComplete() {
                                        super.onComplete();
                                    }
                                }));
                    }
                }));

    }
}
