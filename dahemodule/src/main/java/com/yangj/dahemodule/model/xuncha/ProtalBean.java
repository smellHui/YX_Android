package com.yangj.dahemodule.model.xuncha;

import com.google.common.base.Strings;
import com.yangj.dahemodule.model.main.ReservoirStructure;

import java.util.List;

/**
 * Author:xch
 * Date:2019/9/23
 * Description:
 */
public class ProtalBean {


    /**
     * status : 0
     * createBy : 5
     * creatorName : dhxc
     * createTime : 2019-09-18 10:07:34
     * updateBy : 5
     * updaterName : null
     * updateTime : 2019-09-18 10:07:51
     * id : 1174142956149153794
     * reservoirCode : yjdhsk
     * code : 71f3cd0a7b484161abb081b15a011a78
     * name : 20190918巡查记录表
     * omRouteId : 1
     * omPath : [{114.32277692309223,30.580627284770316},{114.32277692309223,30.580627284770316}]
     * executeStatus : 2
     * routePath : [{114.32282561249224,30.58051265013646},{114.32302375555469,30.5806923213626},{114.3233344066879,30.580414723890577},{114.32314697214893,30.58026734854692},{114.3228363229724,30.580521863001028}]
     */

    private int status;
    private int createBy;
    private String creatorName;
    private String createTime;
    private int updateBy;
    private String updaterName;
    private String updateTime;
    private long id;
    private String reservoirCode;
    private String code;
    private String name;
    private String omRouteId;
    private String omPath;
    private String executeStatus;
    private String routePath;
    private List<ProtalItemBean> itemList;
    private List<ReservoirStructure> reservoirStructureList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public String getCreatorName() {
        return creatorName;
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

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public List<ProtalItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ProtalItemBean> itemList) {
        this.itemList = itemList;
    }

    public List<ReservoirStructure> getReservoirStructureList() {
        return reservoirStructureList;
    }

    public void setReservoirStructureList(List<ReservoirStructure> reservoirStructureList) {
        this.reservoirStructureList = reservoirStructureList;
    }

    public boolean isExecuting() {
        return Strings.isNullOrEmpty(executeStatus) || executeStatus.equals("1");
    }

    @Override
    public String toString() {
        return "ProtalBean{" +
                "status=" + status +
                ", createBy=" + createBy +
                ", creatorName='" + creatorName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy=" + updateBy +
                ", updaterName='" + updaterName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id=" + id +
                ", reservoirCode='" + reservoirCode + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", omRouteId=" + omRouteId +
                ", omPath='" + omPath + '\'' +
                ", executeStatus=" + executeStatus +
                ", routePath='" + routePath + '\'' +
                ", itemList=" + itemList +
                ", reservoirStructureList=" + reservoirStructureList +
                '}';
    }
}
