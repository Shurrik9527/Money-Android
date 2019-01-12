package com.moyacs.canary.login.contract;

import android.util.Base64;

import com.moyacs.canary.network.ServerApi;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.ToastUtils;

import java.io.UnsupportedEncodingException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：luoshen on 2018/3/5 0005 16:22
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class LoginModulImpl implements LoginContract.LoginModul {

    private LoginContract.LoginRequestListener listener;
    private CompositeDisposable mCompositeDisposable;
    private ServerApi loginServer;

    public LoginModulImpl(LoginContract.LoginRequestListener listener) {
        this.listener = listener;
        mCompositeDisposable = new CompositeDisposable();
        loginServer = ServerManger.getInstance().create(ServerApi.class);
    }

    @Override
    public void unsubscribe() {
        //解除所有订阅
        mCompositeDisposable.clear();
    }


    @Override
    public void doLogin(String userName, String passWord) {
        String pw = "zst" + passWord.trim() + "013";
        String base64Pw;
        try {
            base64Pw = Base64.encodeToString(pw.getBytes("utf-8"), Base64.DEFAULT).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ToastUtils.showShort("密码格式输入错误");
            return;
        }
        /*loginServer.doLogin(userName, base64Pw)
                .subscribeOn(Schedulers.io())//指定网络请求所在的线程
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.beforeRequest();
                    }
                })
                .doOnNext(new Consumer<Response<ServerResult<String>>>() {
                    @Override
                    public void accept(Response<ServerResult<String>> httpResultResponse) throws Exception {
                        int code = httpResultResponse.code();
                        if (code == 200) {
                            listener.doOnNext(httpResultResponse.body());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        listener.afterRequest();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//指定的是它之后（下方）执行的操作所在的线程
                .subscribe(new Observer<Response<ServerResult<String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ServerResult<String>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        if (code == 200) {
                            listener.LoginResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.LoginResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.LoginResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }

    @Override
    public void getMT4Info() {
       /* loginServer.getMT4Info()
                .subscribeOn(Schedulers.io())//指定网络请求所在的线程
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.beforeRequest();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        listener.afterRequest();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//指定的是它之后（下方）执行的操作所在的线程
                .subscribe(new Observer<Response<ServerResult<MT4Users>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ServerResult<MT4Users>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.getMT4InfoSucess(httpResultResponse.body());
                        } else {
                            try {
                                listener.getMT4InfoFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getMT4InfoFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }

    @Override
    public void getCode(String mobile) {
      /*  loginServer.getCode(mobile)
                .subscribeOn(Schedulers.io())//指定网络请求所在的线程
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.beforeRequest();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        listener.afterRequest();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//指定的是它之后（下方）执行的操作所在的线程
                .subscribe(new Observer<Response<ServerResult<String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ServerResult<String>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        ServerResult<String> httpResult = httpResultResponse.body();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200 && httpResult != null && httpResult.isSuccess()) {
                            listener.getCodeResponseSucessed(httpResult);
                        } else {
                            try {
                                listener.getCodeResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getCodeResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }

    @Override
    public void register(String registerId, String mobile, String password, String fullname, String vcode) {
       /* String pw = "zst" + password.trim() + "013";
        String base64Pw;
        try {
            base64Pw = Base64.encodeToString(pw.getBytes("utf-8"), Base64.DEFAULT).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ToastUtils.showShort("密码格式错误，请更换密码再试");
            return;
        }
        loginServer.register(registerId, mobile, base64Pw, fullname, vcode)
                .subscribeOn(Schedulers.io())//指定网络请求所在的线程
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.beforeRequest();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        listener.afterRequest();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//指定的是它之后（下方）执行的操作所在的线程
                .subscribe(new Observer<Response<ServerResult<String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ServerResult<String>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.registerResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.registerResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.registerResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }

    @Override
    public void uploadPubKey(String pubKey) {
        loginServer.uploadPubKey(pubKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult<String> stringHttpResult) {
                        if (stringHttpResult.isSuccess()) {
                            listener.uploadPubKeySuccess();
                        } else {
                            listener.uploadPubKeyFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.uploadPubKeyFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
