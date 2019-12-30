package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by      android studio
 *
 * @author :      zhang xinhua
 * Date            :       2018-09-21
 * Time            :       11:56
 * Version         :       1.0
 * 功能描述        :
 **/
public class ReservoirBean extends DataSupport implements Serializable {

    private String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    private int code;
    private int count;
    private String msg;

    private String jsonAboutInfo;
    private String updateTimeOfthisData;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getJsonAboutInfo() {
        return jsonAboutInfo;
    }

    public void setJsonAboutInfo(String jsonAboutInfo) {
        this.jsonAboutInfo = jsonAboutInfo;
    }

    public String getUpdateTimeOfthisData() {
        return updateTimeOfthisData;
    }

    public void setUpdateTimeOfthisData(String updateTimeOfthisData) {
        this.updateTimeOfthisData = updateTimeOfthisData;
    }

    /**
     * reservoirId : 66fb3d579d084daf8a7d35d9d9612213
     * reservoirCode : NTC520321019L
     * reservoirProduce :
     * areaCode : 520321
     * reservoir : 绿竹坝水库
     * reservoirType : 2
     * reservoirAddress : 贵州省播州区乐山镇
     * reservoirLongitude : 106.684497
     * reservoirLatitude : 27.669843
     * manageDepId : 遵义市水利局
     * locatedBasin :
     * locatedRiver :
     * locatedBrook : 乐民河上游
     * mainFunction : 防洪、灌溉、旅游
     * buildStartDate : 2012-11-01
     * buildEndDate : 2015-03-01
     * normalImpoundedLevel : 983
     * damType : 0
     * damLength : 186.5
     * damHeight : 48
     * damWidth : 7
     * damCrestElevation : 986
     * damBotmMaxWidth : 132.61
     * deadWaterLevel : 954
     * deathLevelVolume : 15
     * waterLevelVolume : 463
     * effectiveVolume : 448
     * designFloodWaterLevel : 984.63
     * checkFloodWaterLevel : 985.37
     * rainwaterCollectingArea : 448
     * capacityCoefficient : 0.53
     * lrrigatedFarmlandArea : 43280
     * avgIrrigationWaterSupply : 528
     * drinkingWaterSupply : 39.8
     * avgTotalWaterSupply : 568
     * utilizationRateOfDevelopment : 66.7
     * spillwayType : 开敞式溢洪道
     * spillwayLength : 197
     * diversionCanalLength : 32
     * canalBottomElevation : 981.5
     * overflowMode : 自由溢流
     * weirCrestElevation : 983
     * remarks : 80002002
     * status : 0
     * createDate : 2018-08-14 22:51:04
     * updateBy : 超级管理员
     * updateDate : 2018-08-21 11:30:39
     * areaName : 播州区
     */


    /*   *//**
     * 水库名
     *//*
    private String reservoir;
    private String reservoirProduce;
    private String reservoirType;
    private String areaName;
    *//**
     * 水库地址
     *//*
    private String reservoirAddress;
    private String reservoirLongitude;
    private String reservoirLatitude;
    *//**
     * 主要功能
     *//*
    private String mainFunction;
    *//**
     * 开始建设时间
     *//*
    private String buildStartDate;
    *//**
     * 竣工时间
     *//*
    private String buildEndDate;
    *//**
     * 正常蓄水位
     *//*
    private String normalImpoundedLevel;
    *//**
     * 大坝类型
     *//*
    private String damType;
    *//**
     * 坝长
     *//*
    private String damLength;
    *//**
     * 坝高
     *//*
    private String damHeight;
    *//**
     * 坝宽
     *//*
    private String damWidth;
    *//**
     * 坝顶高程
     *//*
    private String damCrestElevation;
    *//**
     * 坝底最大宽度
     *//*
    private String damBotmMaxWidth;
    */
    /**
     * 库容系数
     *//*
    private String capacityCoefficient;*/

    private String reservoirId;
    private String reservoirCode;
    private String reservoirProduce;
    private String areaCode;
    private String reservoir;
    private String reservoirType;
    private String reservoirAddress;
    private String reservoirLongitude;
    private String reservoirLatitude;
    private String manageDepId;
    private String locatedBasin;
    private String locatedRiver;
    private String locatedBrook;
    private String mainFunction;
    private String buildStartDate;
    private String buildEndDate;
    private String normalImpoundedLevel;
    private String damType;
    private String damLength;
    private String damHeight;
    private String damWidth;
    private String damCrestElevation;
    private String damBotmMaxWidth;
    private String deadWaterLevel;
    private String deathLevelVolume;
    private String waterLevelVolume;
    private String effectiveVolume;
    private String designFloodWaterLevel;
    private String checkFloodWaterLevel;
    private String rainwaterCollectingArea;
    private String capacityCoefficient;
    private String lrrigatedFarmlandArea;
    private String avgIrrigationWaterSupply;
    private String drinkingWaterSupply;
    private String avgTotalWaterSupply;
    private String utilizationRateOfDevelopment;
    private String spillwayType;
    private String spillwayLength;
    private String diversionCanalLength;
    private String canalBottomElevation;
    private String overflowMode;
    private String weirCrestElevation;
    private String remarks;
    private String status;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String areaName;
    private String vrUrl;
    private String lastTime;
    private double distance;//与当前水库的距离(Km)
    //identifyType 坝等级    drainMethod  泄水方式
    private String identifyType;
    private String drainMethod;
    private boolean isOfflineMap;//是否下载了离线地图包

    public boolean isOfflineMap() {
        return isOfflineMap;
    }

    public void setOfflineMap(boolean offlineMap) {
        isOfflineMap = offlineMap;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getDrainMethod() {
        return drainMethod;
    }

    public void setDrainMethod(String drainMethod) {
        this.drainMethod = drainMethod;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getVrUrl() {
        return vrUrl;
    }

    public void setVrUrl(String vrUrl) {
        this.vrUrl = vrUrl;
    }

    /**
     * reservoirId : 66fb3d579d084daf8a7d35d9d9612213
     * reservoir : 绿竹坝水库
     * files : [{"fileId":"f5429bcc554f4a88a3d67116de259bf5","filePath":"http://tepia-skgj.oss-cn-beijing.aliyuncs.com/PC/reservoirAttachment/2018-09/28/水库.jpg"}]
     */

    private List<FilesBean> files;

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getReservoirCode() {
        return reservoirCode;
    }

    public void setReservoirCode(String reservoirCode) {
        this.reservoirCode = reservoirCode;
    }

    public String getReservoirProduce() {
        return reservoirProduce;
    }

    public void setReservoirProduce(String reservoirProduce) {
        this.reservoirProduce = reservoirProduce;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getReservoir() {
        return reservoir;
    }

    public void setReservoir(String reservoir) {
        this.reservoir = reservoir;
    }

    public String getReservoirType() {
        return reservoirType;
    }

    public void setReservoirType(String reservoirType) {
        this.reservoirType = reservoirType;
    }

    public String getReservoirAddress() {
        return reservoirAddress;
    }

    public void setReservoirAddress(String reservoirAddress) {
        this.reservoirAddress = reservoirAddress;
    }

    public String getReservoirLongitude() {
        return reservoirLongitude;
    }

    public void setReservoirLongitude(String reservoirLongitude) {
        this.reservoirLongitude = reservoirLongitude;
    }

    public String getReservoirLatitude() {
        return reservoirLatitude;
    }

    public void setReservoirLatitude(String reservoirLatitude) {
        this.reservoirLatitude = reservoirLatitude;
    }

    public String getManageDepId() {
        return manageDepId;
    }

    public void setManageDepId(String manageDepId) {
        this.manageDepId = manageDepId;
    }

    public String getLocatedBasin() {
        return locatedBasin;
    }

    public void setLocatedBasin(String locatedBasin) {
        this.locatedBasin = locatedBasin;
    }

    public String getLocatedRiver() {
        return locatedRiver;
    }

    public void setLocatedRiver(String locatedRiver) {
        this.locatedRiver = locatedRiver;
    }

    public String getLocatedBrook() {
        return locatedBrook;
    }

    public void setLocatedBrook(String locatedBrook) {
        this.locatedBrook = locatedBrook;
    }

    public String getMainFunction() {
        return mainFunction;
    }

    public void setMainFunction(String mainFunction) {
        this.mainFunction = mainFunction;
    }

    public String getBuildStartDate() {
        return buildStartDate;
    }

    public void setBuildStartDate(String buildStartDate) {
        this.buildStartDate = buildStartDate;
    }

    public String getBuildEndDate() {
        return buildEndDate;
    }

    public void setBuildEndDate(String buildEndDate) {
        this.buildEndDate = buildEndDate;
    }

    public String getNormalImpoundedLevel() {
        return normalImpoundedLevel;
    }

    public void setNormalImpoundedLevel(String normalImpoundedLevel) {
        this.normalImpoundedLevel = normalImpoundedLevel;
    }

    public String getDamType() {
        return damType;
    }

    public void setDamType(String damType) {
        this.damType = damType;
    }

    public String getDamLength() {
        return damLength;
    }

    public void setDamLength(String damLength) {
        this.damLength = damLength;
    }

    public String getDamHeight() {
        return damHeight;
    }

    public void setDamHeight(String damHeight) {
        this.damHeight = damHeight;
    }

    public String getDamWidth() {
        return damWidth;
    }

    public void setDamWidth(String damWidth) {
        this.damWidth = damWidth;
    }

    public String getDamCrestElevation() {
        return damCrestElevation;
    }

    public void setDamCrestElevation(String damCrestElevation) {
        this.damCrestElevation = damCrestElevation;
    }

    public String getDamBotmMaxWidth() {
        return damBotmMaxWidth;
    }

    public void setDamBotmMaxWidth(String damBotmMaxWidth) {
        this.damBotmMaxWidth = damBotmMaxWidth;
    }

    public String getDeadWaterLevel() {
        return deadWaterLevel;
    }

    public void setDeadWaterLevel(String deadWaterLevel) {
        this.deadWaterLevel = deadWaterLevel;
    }

    public String getDeathLevelVolume() {
        return deathLevelVolume;
    }

    public void setDeathLevelVolume(String deathLevelVolume) {
        this.deathLevelVolume = deathLevelVolume;
    }

    public String getWaterLevelVolume() {
        return waterLevelVolume;
    }

    public void setWaterLevelVolume(String waterLevelVolume) {
        this.waterLevelVolume = waterLevelVolume;
    }

    public String getEffectiveVolume() {
        return effectiveVolume;
    }

    public void setEffectiveVolume(String effectiveVolume) {
        this.effectiveVolume = effectiveVolume;
    }

    public String getDesignFloodWaterLevel() {
        return designFloodWaterLevel;
    }

    public void setDesignFloodWaterLevel(String designFloodWaterLevel) {
        this.designFloodWaterLevel = designFloodWaterLevel;
    }

    public String getCheckFloodWaterLevel() {
        return checkFloodWaterLevel;
    }

    public void setCheckFloodWaterLevel(String checkFloodWaterLevel) {
        this.checkFloodWaterLevel = checkFloodWaterLevel;
    }

    public String getRainwaterCollectingArea() {
        return rainwaterCollectingArea;
    }

    public void setRainwaterCollectingArea(String rainwaterCollectingArea) {
        this.rainwaterCollectingArea = rainwaterCollectingArea;
    }

    public String getCapacityCoefficient() {
        return capacityCoefficient;
    }

    public void setCapacityCoefficient(String capacityCoefficient) {
        this.capacityCoefficient = capacityCoefficient;
    }

    public String getLrrigatedFarmlandArea() {
        return lrrigatedFarmlandArea;
    }

    public void setLrrigatedFarmlandArea(String lrrigatedFarmlandArea) {
        this.lrrigatedFarmlandArea = lrrigatedFarmlandArea;
    }

    public String getAvgIrrigationWaterSupply() {
        return avgIrrigationWaterSupply;
    }

    public void setAvgIrrigationWaterSupply(String avgIrrigationWaterSupply) {
        this.avgIrrigationWaterSupply = avgIrrigationWaterSupply;
    }

    public String getDrinkingWaterSupply() {
        return drinkingWaterSupply;
    }

    public void setDrinkingWaterSupply(String drinkingWaterSupply) {
        this.drinkingWaterSupply = drinkingWaterSupply;
    }

    public String getAvgTotalWaterSupply() {
        return avgTotalWaterSupply;
    }

    public void setAvgTotalWaterSupply(String avgTotalWaterSupply) {
        this.avgTotalWaterSupply = avgTotalWaterSupply;
    }

    public String getUtilizationRateOfDevelopment() {
        return utilizationRateOfDevelopment;
    }

    public void setUtilizationRateOfDevelopment(String utilizationRateOfDevelopment) {
        this.utilizationRateOfDevelopment = utilizationRateOfDevelopment;
    }

    public String getSpillwayType() {
        return spillwayType;
    }

    public void setSpillwayType(String spillwayType) {
        this.spillwayType = spillwayType;
    }

    public String getSpillwayLength() {
        return spillwayLength;
    }

    public void setSpillwayLength(String spillwayLength) {
        this.spillwayLength = spillwayLength;
    }

    public String getDiversionCanalLength() {
        return diversionCanalLength;
    }

    public void setDiversionCanalLength(String diversionCanalLength) {
        this.diversionCanalLength = diversionCanalLength;
    }

    public String getCanalBottomElevation() {
        return canalBottomElevation;
    }

    public void setCanalBottomElevation(String canalBottomElevation) {
        this.canalBottomElevation = canalBottomElevation;
    }

    public String getOverflowMode() {
        return overflowMode;
    }

    public void setOverflowMode(String overflowMode) {
        this.overflowMode = overflowMode;
    }

    public String getWeirCrestElevation() {
        return weirCrestElevation;
    }

    public void setWeirCrestElevation(String weirCrestElevation) {
        this.weirCrestElevation = weirCrestElevation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservoirBean that = (ReservoirBean) o;
        return Objects.equals(reservoirId, that.reservoirId);
    }


    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public static class FilesBean {
        /**
         * fileId : f5429bcc554f4a88a3d67116de259bf5
         * filePath : http://tepia-skgj.oss-cn-beijing.aliyuncs.com/PC/reservoirAttachment/2018-09/28/水库.jpg
         */

        private String fileId;
        private String filePath;

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
