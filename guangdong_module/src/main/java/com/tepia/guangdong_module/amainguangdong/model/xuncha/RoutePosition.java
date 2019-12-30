package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import android.text.TextUtils;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by      Android studio
 *
 * @author :wwj (from Center Of Wuhan)
 * Date    :2019/5/7
 * Version :1.0
 * 功能描述 : 巡查点
 **/
public class RoutePosition extends DataSupport implements Serializable {

    @Override
    public synchronized boolean saveOrUpdate(String... conditions) {
        return super.saveOrUpdate(conditions);
    }

    String fatherId;

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    String reservoirId;
    String userCode;

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    private String positionId;
    private String structureName;
    private String structurePath;
    private String positionLttd;
    private String positionLgtd;
    private String workOrderId;
    private String uuid;
    //0 未邻近；1 已临近
    @Column(ignore = true)
    private int isNear;

    public int getIsNear() {
        return isNear;
    }

    public void setIsNear(int isNear) {
        this.isNear = isNear;
    }

    public String getStructureName() {
        return TextUtils.isEmpty(structureName) ? "" : structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getStructurePath() {
        return TextUtils.isEmpty(structurePath) ? "" : structurePath;
    }

    public void setStructurePath(String structurePath) {
        this.structurePath = structurePath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionLttd() {
        return positionLttd;
    }

    public void setPositionLttd(String positionLttd) {
        this.positionLttd = positionLttd;
    }

    public String getPositionLgtd() {
        return positionLgtd;
    }

    public void setPositionLgtd(String positionLgtd) {
        this.positionLgtd = positionLgtd;
    }

    public Point parasePoint() {
        try {
            return new Point(Double.parseDouble(positionLgtd), Double.parseDouble(positionLttd), SpatialReference.create(4326));
        } catch (Exception e) {
            return null;
        }
    }
}
