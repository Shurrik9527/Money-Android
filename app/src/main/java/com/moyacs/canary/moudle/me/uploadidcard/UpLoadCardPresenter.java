package com.moyacs.canary.moudle.me.uploadidcard;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.main.me.account.AccountActivity;
import com.moyacs.canary.moudle.me.RealNameAuthContract;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public class UpLoadCardPresenter implements UpLoadIdCardContract.Presenter{

    private static final String TAG = UpLoadCardPresenter.class.getName();

    private final UpLoadIdCardContract.View mView;
    private CompositeDisposable disposable;
    public UpLoadCardPresenter(UpLoadIdCardContract.View view) {
        mView = view;
        mView.setPresenter(this);
        disposable = new CompositeDisposable();
    }


    @Override
    public void unsubscribe() {

    }

    @Override
    public void uploadIdCard(Context context,File file, boolean isBefor, boolean after) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("typeName", AppConstans.toRequestBody("id"));
        List<MultipartBody.Part> bodyList = new ArrayList<>();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
        bodyList.add(body);
        disposable.add(ServerManger.getInstance().getServer().uploadFile(map, bodyList)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        String imgPath = data.getData();
                        if(mView!=null){
                            if(isBefor){
                                mView.uploadBeforImgSuccess(imgPath);
                            }

                            if(after){
                                mView.uploadAfterImgSuccess(imgPath);
                            }
                            mView.showMessageTips("上传成功!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.showMessageTips("上传失败!"+e.getMessage());
                        }
                    }
                }));


    }

    /**
     *
     * @param gender
     * @param birthdate
     * @param id_number
     * @param card_front
     * @param card_reverse
     * @param userName
     */
    @Override
    public void submitRealNameInform(String gender, String birthdate, String id_number, String card_front, String card_reverse, String userName) {

        disposable.add(ServerManger.getInstance().getServer().realNameAuth(gender,birthdate,id_number,card_front,card_reverse,userName).compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {

                        if(mView!=null){
                            mView.showNextResult(true);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.showMessageTips("提交失败!"+e.getMessage());
                        }
                    }
                }));




    }


}
