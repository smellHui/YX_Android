package com.yangj.dahemodule.model.main;

import com.google.common.base.Strings;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class ReservoirInfo {

    private String id;
    private String code;
    private String name;
    private String longitude;
    private String latitude;
    private String projectScale;
    private String projectScaleLabel;
    private String reservoirOverview;
    private String damType;
    private String damTypeLabel;
    private String damLength;
    private String damHeight;
    private String damWidth;
    private String damTopElevation;
    private String startTime;
    private String endTime;
    private String maxDamWidth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getProjectScale() {
        return projectScale;
    }

    public void setProjectScale(String projectScale) {
        this.projectScale = projectScale;
    }

    public String getProjectScaleLabel() {
        return projectScaleLabel;
    }

    public void setProjectScaleLabel(String projectScaleLabel) {
        this.projectScaleLabel = projectScaleLabel;
    }

    public String getReservoirOverview() {
        return reservoirOverview;
    }

    public void setReservoirOverview(String reservoirOverview) {
        this.reservoirOverview = reservoirOverview;
    }

    public String getDamType() {
        return damType;
    }

    public void setDamType(String damType) {
        this.damType = damType;
    }

    public String getDamTypeLabel() {
        return Strings.isNullOrEmpty(damTypeLabel) ? "--" : damTypeLabel;
    }

    public void setDamTypeLabel(String damTypeLabel) {
        this.damTypeLabel = damTypeLabel;
    }

    public String getDamLength() {
        return Strings.isNullOrEmpty(damLength) ? "--" : damLength + "m";
    }

    public void setDamLength(String damLength) {
        this.damLength = damLength;
    }

    public String getDamHeight() {
        return Strings.isNullOrEmpty(damHeight) ? "--" : damHeight + "m";
    }

    public void setDamHeight(String damHeight) {
        this.damHeight = damHeight;
    }

    public String getDamWidth() {
        return Strings.isNullOrEmpty(damWidth) ? "--" : damWidth + "m";
    }

    public void setDamWidth(String damWidth) {
        this.damWidth = damWidth;
    }

    public String getDamTopElevation() {
        return Strings.isNullOrEmpty(damTopElevation) ? "--" : damTopElevation + "m";
    }

    public void setDamTopElevation(String damTopElevation) {
        this.damTopElevation = damTopElevation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMaxDamWidth() {
        return Strings.isNullOrEmpty(maxDamWidth) ? "--" : maxDamWidth + "m";
    }

    public void setMaxDamWidth(String maxDamWidth) {
        this.maxDamWidth = maxDamWidth;
    }

    @Override
    public String toString() {
        return "ReservoirInfo{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", projectScale='" + projectScale + '\'' +
                ", projectScaleLabel='" + projectScaleLabel + '\'' +
                ", reservoirOverview='" + reservoirOverview + '\'' +
                ", damType='" + damType + '\'' +
                ", damTypeLabel='" + damTypeLabel + '\'' +
                ", damLength='" + damLength + '\'' +
                ", damHeight='" + damHeight + '\'' +
                ", damWidth='" + damWidth + '\'' +
                ", damTopElevation='" + damTopElevation + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", maxDamWidth='" + maxDamWidth + '\'' +
                '}';
    }
}
