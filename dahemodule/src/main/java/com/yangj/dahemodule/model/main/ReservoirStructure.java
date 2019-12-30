package com.yangj.dahemodule.model.main;

import org.litepal.crud.DataSupport;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class ReservoirStructure extends DataSupport {

    private String status;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
    private String id;
    private String reservoirCode;
    private String structureId;
    private String structurePath;
    private String positionName;
    private String positionLongitude;
    private String positionLatitude;
    private String structureName;

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

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getStructurePath() {
        return structurePath;
    }

    public void setStructurePath(String structurePath) {
        this.structurePath = structurePath;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionLongitude() {
        return positionLongitude;
    }

    public void setPositionLongitude(String positionLongitude) {
        this.positionLongitude = positionLongitude;
    }

    public String getPositionLatitude() {
        return positionLatitude;
    }

    public void setPositionLatitude(String positionLatitude) {
        this.positionLatitude = positionLatitude;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    @Override
    public String toString() {
        return "ReservoirStructure{" +
                "status='" + status + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id='" + id + '\'' +
                ", reservoirCode='" + reservoirCode + '\'' +
                ", structureId='" + structureId + '\'' +
                ", structurePath='" + structurePath + '\'' +
                ", positionName='" + positionName + '\'' +
                ", positionLongitude='" + positionLongitude + '\'' +
                ", positionLatitude='" + positionLatitude + '\'' +
                ", structureName='" + structureName + '\'' +
                '}';
    }
}
