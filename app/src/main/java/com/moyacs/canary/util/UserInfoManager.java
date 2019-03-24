package com.moyacs.canary.util;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import com.moyacs.canary.MyApplication;
import com.moyacs.canary.bean.UserBean;
import com.moyacs.canary.bean.UserInfoVo;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/23
 * @email 252774645@qq.com
 */
public class UserInfoManager {

    private static UserInfoManager instance;

    private UserBean userBean;

    private UserInfoManager() {

    }


    public static UserInfoManager getInstance() {
        if (instance == null) {
            synchronized (UserInfoManager.class) {
                if (instance == null) {
                    instance = new UserInfoManager();
                }
            }
        }
        return instance;
    }


    /**
     * 获取当前用户信息
     *
     * @return
     */
    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * 设置当前用户信息
     *
     * @param userBean
     */
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public void setUserBean(UserInfoVo mUserInfoVo) {
        if (userBean == null) {
            userBean = new UserBean();
        }
        userBean.setPartyId("");
        if(!TextUtils.isEmpty(mUserInfoVo.getNickname())){
            userBean.setNickName(mUserInfoVo.getNickname());
        }else {
            userBean.setNickName(mUserInfoVo.getLoginName());
        }
        userBean.setHeadImg(mUserInfoVo.getUserImg());
        userBean.setPhoneNum(mUserInfoVo.getLoginName());
    }

    /**
     * 清楚当前用户信息
     */
    public void clearUserInfo() {
        userBean = null;
    }


    /**
     * 保存当前用户信息到内存汇总
     *
     * @param result
     */
    public void saveCurrentUserInfo(UserInfoVo result) {
        setUserBean(result);
        setRongYunUserInfo(result);
    }

    /**
     * 设置融云的用户信息
     *
     * @param result
     */
    public void setRongYunUserInfo(UserInfoVo result) {
        if (result == null) {
            return;
        }
        Uri headImgUri;
        if (TextUtils.isEmpty(result.getUserImg())) { // 头像为空，设置默认头像
            final Resources resources = MyApplication.instance.getResources();
            headImgUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + resources.getResourcePackageName(R.mipmap.img_me_headimage_default) + "/"
                    + resources.getResourceTypeName(R.mipmap.img_me_headimage_default) + "/"
                    + resources.getResourceEntryName(R.mipmap.img_me_headimage_default));
        } else { // 使用服务器返回的头像
            headImgUri = Uri.parse(result.getUserImg());
        }

//        UserInfo userInfo = new UserInfo(
//                String.valueOf(result.getPartyId()),
//                result.getNikerName(),
//                headImgUri
//        );
        UserInfo userInfo =null;
        if(!TextUtils.isEmpty(result.getNickname())){
            userInfo = new UserInfo("",
                    result.getNickname(),
                    headImgUri
            );
        }else {
            userInfo = new UserInfo("",
                    result.getLoginName(),
                    headImgUri
            );
        }

        RongIM.getInstance().setCurrentUserInfo(userInfo);
    }


    public UserInfo converUserInfoDTOToRongyunUser(UserInfoVo result) {

        if (result == null) {
            return null;
        }

        Uri headImgUri = getHeadImgUri(result.getUserImg());

//        UserInfo userInfo = new UserInfo(
//                String.valueOf(result.getPartyId()),
//                result.getNikerName(),
//                headImgUri
//        );
        UserInfo userInfo =null;
        if(!TextUtils.isEmpty(result.getNickname())){
            userInfo = new UserInfo("",
                    result.getNickname(),
                    headImgUri
            );
        }else {
            userInfo = new UserInfo("",
                    result.getLoginName(),
                    headImgUri
            );
        }

        return userInfo;
    }

    public UserInfo converUserBeanRongyunUser(UserBean result) {

        if (result == null) {
            return null;
        }

        Uri headImgUri = getHeadImgUri(result.getHeadImg());

        UserInfo userInfo =null;
        if(!TextUtils.isEmpty(result.getNickName())){
            userInfo = new UserInfo("",
                    result.getNickName(),
                    headImgUri
            );
        }else {
            userInfo = new UserInfo("",
                    result.getPhoneNum(),
                    headImgUri
            );
        }


        return userInfo;
    }


    public Uri getHeadImgUri(String url) {
        Uri headImgUri;
        if (TextUtils.isEmpty(url)) { // 头像为空，设置默认头像
            final Resources resources = MyApplication.instance.getResources();
            headImgUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + resources.getResourcePackageName(R.mipmap.img_me_headimage_default) + "/"
                    + resources.getResourceTypeName(R.mipmap.img_me_headimage_default) + "/"
                    + resources.getResourceEntryName(R.mipmap.img_me_headimage_default));
        } else { // 使用服务器返回的头像
            headImgUri = Uri.parse(url);
        }

        return headImgUri;
    }

}
