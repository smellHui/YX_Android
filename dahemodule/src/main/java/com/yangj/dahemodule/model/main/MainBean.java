package com.yangj.dahemodule.model.main;

import com.tepia.guangdong_module.amainguangdong.model.DangerousPosition;
import com.tepia.guangdong_module.amainguangdong.model.UserInfo;

import java.util.List;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class MainBean {
    private List<UserInfo> userList;
    private List<Route> routeList;
    private ReservoirInfo reservoirInfo;
    private List<DangerousPosition> dangerousPositionList;

    public List<UserInfo> getUserList() {
        return userList;
    }

    public void setUserList(List<UserInfo> userList) {
        this.userList = userList;
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    public ReservoirInfo getReservoirInfo() {
        return reservoirInfo;
    }

    public void setReservoirInfo(ReservoirInfo reservoirInfo) {
        this.reservoirInfo = reservoirInfo;
    }

    public List<DangerousPosition> getDangerousPositionList() {
        return dangerousPositionList;
    }

    public void setDangerousPositionList(List<DangerousPosition> dangerousPositionList) {
        this.dangerousPositionList = dangerousPositionList;
    }

    @Override
    public String toString() {
        return "MainBean{" +
                "userList=" + userList +
                ", routeList=" + routeList +
                ", reservoirInfo=" + reservoirInfo +
                ", dangerousPositionList=" + dangerousPositionList +
                '}';
    }
}
