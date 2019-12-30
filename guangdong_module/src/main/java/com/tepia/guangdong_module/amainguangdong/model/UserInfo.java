package com.tepia.guangdong_module.amainguangdong.model;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class UserInfo {
    private String username;
    private String phone;
    private String alias;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
