package com.moyacs.canary.bean;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/21
 * @email 252774645@qq.com
 */
public class UserInformBean implements Serializable {

    private String phone;
    private String name;
    private String sex;
    private String age;
    private String address;
    private String idcard;
    private String idcard_beforPath;
    private String idcard_afterPath;
    private boolean isAuth;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcard_beforPath() {
        return idcard_beforPath;
    }

    public void setIdcard_beforPath(String idcard_beforPath) {
        this.idcard_beforPath = idcard_beforPath;
    }

    public String getIdcard_afterPath() {
        return idcard_afterPath;
    }

    public void setIdcard_afterPath(String idcard_afterPath) {
        this.idcard_afterPath = idcard_afterPath;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    @Override
    public String toString() {
        return "UserInformBean{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                ", idcard='" + idcard + '\'' +
                ", idcard_beforPath='" + idcard_beforPath + '\'' +
                ", idcard_afterPath='" + idcard_afterPath + '\'' +
                ", isAuth=" + isAuth +
                '}';
    }
}
