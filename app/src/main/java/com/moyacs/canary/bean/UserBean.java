/*
 * 乡邻小站
 *  Copyright (c) 2017 XiangLin,Inc.All Rights Reserved.
 */

package com.moyacs.canary.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 用户信息
 * @date 2019/3/23
 * @email 252774645@qq.com
 */
public class UserBean {

    private String partyId;
    private String nickName;
    private String headImg;
    private String phoneNum;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
