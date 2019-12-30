package com.yangj.dahemodule.model;

import com.tepia.base.http.BaseResponse;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-16
 * Time            :       上午12:33
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class UserLoginResponse extends BaseResponse {
    /**
     * userName : ZYSYWZXGCXJ1
     * userId : 9517D806FD5D4CF09AB0E6206281F84D
     * sparent : dba41afabdbc484c879faf07411e2a18
     * orgId : null
     * name : 遵义市运维中心工程巡检1
     * phone : 18511836685
     * email :
     * address :
     */

    /**
     * {
     * "access_token":"4e0c0564-5ae0-4048-8b56-6248d2a8f16d",
     * "token_type":"bearer",
     * "refresh_token":"ec584730-2a5d-4cfe-b07d-0f125ff4f082",
     * "expires_in":42652,
     * "scope":"server",
     * "license":"made by Tepia"
     * }
     */

    private String data;
    private String userName;
    private String userPass;
    private String userId;
    private String sparent;
    private Object orgId;
    private String name;
    private String phone;
    private String email;
    private String address;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSparent() {
        return sparent;
    }

    public void setSparent(String sparent) {
        this.sparent = sparent;
    }

    public Object getOrgId() {
        return orgId;
    }

    public void setOrgId(Object orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserLoginResponse{" +
                "data='" + data + '\'' +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", userId='" + userId + '\'' +
                ", sparent='" + sparent + '\'' +
                ", orgId=" + orgId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
