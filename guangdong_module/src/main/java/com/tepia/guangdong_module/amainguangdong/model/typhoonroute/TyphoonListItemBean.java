package com.tepia.guangdong_module.amainguangdong.model.typhoonroute;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/5/31
 * Time :    11:22
 * Describe :
 */
public class TyphoonListItemBean {

    /**
     * warnLevel: white,
     * centerLng: 105.350000,
     * isActive: 0,
     * name: 帕布,
     * centerLat: 12.250000,
     * startTime: 2018/12/31 17:00:00,
     * endTime: 2019/1/5 8:00:00,
     * tfId: 201901,
     * enName: PABUK,
     * land: 
     */

    private String warnLevel;
    private String centerLng;
    private String isActive;
    private String name;
    private String centerLat;
    private String startTime;
    private String endTime;
    private String tfId;
    private String enName;
    private String land;
    private TyphoonRouteItemBean typhoonPointInfo;

    public TyphoonRouteItemBean getTyphoonPointInfo() {
        return typhoonPointInfo;
    }

    public void setTyphoonPointInfo(TyphoonRouteItemBean typhoonPointInfo) {
        this.typhoonPointInfo = typhoonPointInfo;
    }

    public String getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }

    public String getCenterLng() {
        return centerLng;
    }

    public void setCenterLng(String centerLng) {
        this.centerLng = centerLng;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(String centerLat) {
        this.centerLat = centerLat;
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

    public String getTfId() {
        return tfId;
    }

    public void setTfId(String tfId) {
        this.tfId = tfId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }
}
