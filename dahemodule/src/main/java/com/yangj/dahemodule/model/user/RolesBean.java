package com.yangj.dahemodule.model.user;

import com.google.common.base.Strings;

/**
 * Author:xch
 * Date:2019/10/16
 * Description:
 */
public class RolesBean {

    private String roleId;
    private String roleName;
    /**
     * 0-管理员 1-行政责任人 2-技术责任人 3-巡查责任人
     */
    private String roleType;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    /**
     * 是否是巡查责任人
     *
     * @return
     */
    public boolean isXC() {
        return !Strings.isNullOrEmpty(roleType) && roleType.equals("3");
    }

    @Override
    public String toString() {
        return "RolesBean{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleType='" + roleType + '\'' +
                '}';
    }
}
