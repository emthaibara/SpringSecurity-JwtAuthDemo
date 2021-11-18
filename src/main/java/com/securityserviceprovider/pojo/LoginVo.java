package com.securityserviceprovider.pojo;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */

public class LoginVo {

    private String phoneNumber;

    private String password;

    @Override
    public String toString() {
        return "LoginUser{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
