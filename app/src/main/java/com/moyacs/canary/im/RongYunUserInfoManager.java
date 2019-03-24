package com.moyacs.canary.im;

import android.content.Context;
import android.util.Log;
import com.moyacs.canary.util.UserInfoManager;
import java.util.LinkedHashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 获取用户信息
 * @date 2019/3/21
 * @email 252774645@qq.com
 */
public class RongYunUserInfoManager {

    private static final String TAG =RongYunUserInfoManager.class.getName();
    private static RongYunUserInfoManager sInstance;

    private LinkedHashMap<String, UserInfo> mUserInfoCache;

    public RongYunUserInfoManager(Context context) {
        mUserInfoCache = new LinkedHashMap<>();
    }

    public static RongYunUserInfoManager getInstance() {
        return sInstance;
    }

    public static void init(Context context) {
        sInstance = new RongYunUserInfoManager(context);
    }

    public void saveUserInfo(String id, UserInfo userInfo) {
        if (mUserInfoCache == null) return;
        mUserInfoCache.put(id, userInfo);
    }

    /**
     * 提供用户信息的用户信息提供者
     * 1.读缓存
     * 2.网络获取
     */
    public void getUserInfo(final String partyId) {

        if (UserInfoManager.getInstance() != null && mUserInfoCache != null) {
            UserInfo userInfo = mUserInfoCache.get(partyId);
            if (userInfo != null) {
                RongIM.getInstance().refreshUserInfoCache(userInfo);
                Log.d(TAG,"ChatAppContext getUserInfo from  cache " + partyId + " "
                        + userInfo.getName() + " " + userInfo.getPortraitUri());
                return;
            }
        }

//        ChatUserInfoRespository.provideTasksRepository().getUserByPartyId(partyId)
//                .compose(TransformUtils.defaultSchedulers()).subscribe(new HttpResponseSubscriber<UserInfoVo>() {
//            @Override
//            public void onSuccess(UserInfoVo result) {
//
//                if (result == null) {
//                    return;
//                }
//
//                UserInfo userInfo = UserInfoManager.getInstance().converUserInfoDTOToRongyunUser(result);
//
//                Log.d(TAG,"ChatAppContext getUserInfo from XL server " + partyId + " "
//                        + userInfo.getName() + " " + userInfo.getPortraitUri());
//
//                RongIM.getInstance().refreshUserInfoCache(userInfo);
//
//            }
//
//            @Override
//            public void _onError(Throwable e) {
//            }
//        });


    }

}
