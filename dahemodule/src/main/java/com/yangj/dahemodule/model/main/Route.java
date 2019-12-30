package com.yangj.dahemodule.model.main;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class Route extends DataSupport implements Serializable {

    private int status;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
    private String id;
    private String omType;
    private String reservoirCode;
    /**
     * 1：日常；
     * 2：定期；
     * 3：特别；
     * 4：自定义
     */
    private String type;
    private String name;
    private String path;
    private String coverPath;
    private List<ReservoirStructure> reservoirStructureList;
    private List<Omltem> omItemList;

    @Override
    public String toString() {
        return "Route{" +
                "status=" + status +
                ", createBy='" + createBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id='" + id + '\'' +
                ", omType='" + omType + '\'' +
                ", reservoirCode='" + reservoirCode + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", reservoirStructureList=" + reservoirStructureList +
                ", omItemList=" + omItemList +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    public String getOmType() {
        return omType;
    }

    public void setOmType(String omType) {
        this.omType = omType;
    }

    public String getReservoirCode() {
        return reservoirCode;
    }

    public void setReservoirCode(String reservoirCode) {
        this.reservoirCode = reservoirCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public List<ReservoirStructure> getReservoirStructureList() {
        return reservoirStructureList;
    }

    public void setReservoirStructureList(List<ReservoirStructure> reservoirStructureList) {
        this.reservoirStructureList = reservoirStructureList;
    }

    public List<Omltem> getOmItemList() {
        return omItemList;
    }

    public void setOmItemList(List<Omltem> omItemList) {
        this.omItemList = omItemList;
    }
}
