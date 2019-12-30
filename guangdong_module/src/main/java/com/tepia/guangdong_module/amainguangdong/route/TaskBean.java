package com.tepia.guangdong_module.amainguangdong.route;

import com.tepia.base.CacheConsts;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.photo_picker.utils.SPUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by      Android studio
 *
 * @author :ly (from Center Of Wuhan)
 * 创建时间 :2019-5-8
 * 更新时间 :
 * Version :1.0
 * 功能描述 :工单
 **/

public class TaskBean extends DataSupport implements Serializable {

    @Override
    public synchronized boolean saveOrUpdate(String... conditions) {
//        boolean save = routeListBean.saveOrUpdate("onlyid=? and userCode=? and reservoirId=? and workOrderId=?",routeListBean.getId(),userCode,reservoirId,workOrderId);

        return super.saveOrUpdate(conditions);
    }

    private String roleName;
    private String createDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    private String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


    @Column(unique = true, nullable = false)
    private String workOrderId;
    private String routeId;
    private String reservoirId;
    private String reservoir;

    private String startTime;

    /**
     * 路线
     */
    private String workOrderRoute;
    private String routeName;
    private String routeType;


    /**
     * 工单执行人code
     */
    private String executorName;
    private String operationType;
    /**
     * 执行状态  2 未完成 3已完成
     */
    private String executeStatus;
    private String reservoirName;


    /**
     * 路线信息
     */
    private RouteListBean routeListBean;

    public RouteListBean getRouteListBean() {

        String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
        String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
        routeListBean = DataSupport.where("onlyid=? and usercode=? and reservoirid=?", routeId, workOrderId, userCode, reservoirId).findFirst(RouteListBean.class);


        return routeListBean;
    }

    public RouteListBean getRouteListBeanByWorkId(String workOrderId) {
        RouteListBean routeListBeanNew = DataSupport.where("workorderid=?", workOrderId).findFirst(RouteListBean.class);
        return routeListBeanNew;
    }

    public void setRouteListBean(RouteListBean routeListBean) {
        this.routeListBean = routeListBean;
    }

    private boolean hasCreated;

    public boolean isHasCreated() {
        return hasCreated;
    }

    public void setHasCreated(boolean hasCreated) {
        this.hasCreated = hasCreated;
    }

    /**
     * 判断是否已完成
     */
    public boolean isHasExecuted() {
        return executeStatus != null && executeStatus.equals("3");
    }

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }


    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }


    public String getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getReservoir() {
        return reservoir;
    }

    public void setReservoir(String reservoir) {
        this.reservoir = reservoir;
    }

    public String getWorkOrderRoute() {
        return workOrderRoute;
    }

    public void setWorkOrderRoute(String workOrderRoute) {
        this.workOrderRoute = workOrderRoute;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }


    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }
}
