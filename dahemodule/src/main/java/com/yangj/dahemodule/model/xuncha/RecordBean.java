package com.yangj.dahemodule.model.xuncha;

import android.text.TextUtils;

import com.google.common.base.Strings;

/**
 * Author:xch
 * Date:2019/9/3
 * Description:巡查列表项
 */
public class RecordBean {
    //临时
    private String temporary;
    private String status;
    private String createBy;
    private String creatorName;
    private String createTime;
    private String updateBy;
    private String updaterName;
    private String updateTime;
    private String id;
    private String reservoirCode;
    private String code;
    private String name;
    private String omRouteId;
    private String omPath;
    /**
     * 1-执行中
     * 2-已提交
     * 3-运维已审核
     * 4-已生成报告
     */
    private String executeStatus;
    private String abnormalNum;
    private String routeType;
    private String routePath;
    private String itemList;
    private String reservoirStructureList;

    public boolean isTemporary() {
        return !TextUtils.isEmpty(temporary) && temporary.equals("1");
    }

    public String getTemporary() {
        return temporary;
    }

    public void setTemporary(String temporary) {
        this.temporary = temporary;
    }

    public String getRouteType() {
        return Strings.isNullOrEmpty(routeType) ? "1" : routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getAbnormalNum() {
        return Strings.isNullOrEmpty(abnormalNum) ? "0" : abnormalNum;
    }

    public void setAbnormalNum(String abnormalNum) {
        this.abnormalNum = abnormalNum;
    }

    public boolean isZeroAbnormal() {
        return Strings.isNullOrEmpty(abnormalNum) || abnormalNum.equals("0");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreatorName() {
        return Strings.nullToEmpty(creatorName);
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservoirCode() {
        return reservoirCode;
    }

    public void setReservoirCode(String reservoirCode) {
        this.reservoirCode = reservoirCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOmRouteId() {
        return omRouteId;
    }

    public void setOmRouteId(String omRouteId) {
        this.omRouteId = omRouteId;
    }

    public String getOmPath() {
        return omPath;
    }

    public void setOmPath(String omPath) {
        this.omPath = omPath;
    }

    public String getExecuteStatus() {
        return Strings.nullToEmpty(executeStatus);
    }

    public void setExecuteStatus(String executeStatus) {
        this.executeStatus = executeStatus;
    }

    /**
     * 正在执行中
     *
     * @return
     */
    public boolean isExecuting() {
        return Strings.isNullOrEmpty(executeStatus) || executeStatus.equals("1");
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public String getReservoirStructureList() {
        return reservoirStructureList;
    }

    public void setReservoirStructureList(String reservoirStructureList) {
        this.reservoirStructureList = reservoirStructureList;
    }

    @Override
    public String toString() {
        return "RecordBean{" +
                "status='" + status + '\'' +
                ", createBy='" + createBy + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id='" + id + '\'' +
                ", reservoirCode='" + reservoirCode + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", omRouteId='" + omRouteId + '\'' +
                ", omPath='" + omPath + '\'' +
                ", executeStatus='" + executeStatus + '\'' +
                ", abnormalNum='" + abnormalNum + '\'' +
                ", routePath='" + routePath + '\'' +
                ", itemList='" + itemList + '\'' +
                ", reservoirStructureList='" + reservoirStructureList + '\'' +
                '}';
    }
}
