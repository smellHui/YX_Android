package com.yangj.dahemodule.model.main;

import org.litepal.crud.DataSupport;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class Omltem extends DataSupport {

    private String positionName;
    private String structurePath;
    private String omItemId;
    private String omItemContent;
    private String reservoirStructureId;
    private String omItemLevel;
    private String omItemName;

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getStructurePath() {
        return structurePath;
    }

    public void setStructurePath(String structurePath) {
        this.structurePath = structurePath;
    }

    public String getOmItemId() {
        return omItemId;
    }

    public void setOmItemId(String omItemId) {
        this.omItemId = omItemId;
    }

    public String getOmItemContent() {
        return omItemContent;
    }

    public void setOmItemContent(String omItemContent) {
        this.omItemContent = omItemContent;
    }

    public String getReservoirStructureId() {
        return reservoirStructureId;
    }

    public void setReservoirStructureId(String reservoirStructureId) {
        this.reservoirStructureId = reservoirStructureId;
    }

    public String getOmItemLevel() {
        return omItemLevel;
    }

    public void setOmItemLevel(String omItemLevel) {
        this.omItemLevel = omItemLevel;
    }

    public String getOmItemName() {
        return omItemName;
    }

    public void setOmItemName(String omItemName) {
        this.omItemName = omItemName;
    }

    @Override
    public String toString() {
        return "Omltem{" +
                "positionName='" + positionName + '\'' +
                ", structurePath='" + structurePath + '\'' +
                ", omItemId='" + omItemId + '\'' +
                ", omItemContent='" + omItemContent + '\'' +
                ", reservoirStructureId='" + reservoirStructureId + '\'' +
                ", omItemLevel='" + omItemLevel + '\'' +
                ", omItemName='" + omItemName + '\'' +
                '}';
    }
}
