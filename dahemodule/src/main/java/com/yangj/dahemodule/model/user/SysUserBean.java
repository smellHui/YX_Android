package com.yangj.dahemodule.model.user;

import java.util.List;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class SysUserBean {

    private String  userId;
    private String  username;
    private String  nickName;
    private String  positionName;
    private String  email;
    private String  mobile;
    private String  sex;
    private String  description;
    private String  avatar;
    private String  deptIds;
    private List<RolesBean> roleList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public List<RolesBean> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RolesBean> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "SysUserBean{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", positionName='" + positionName + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", sex='" + sex + '\'' +
                ", description='" + description + '\'' +
                ", avatar='" + avatar + '\'' +
                ", deptIds='" + deptIds + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}
