package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import org.litepal.crud.DataSupport;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-07
 * Time            :       上午10:13
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class DangerBean extends DataSupport {
    String reservoirId;
    String reservoir;
    String positionName;
    String problemDescription;
    String userCode;
    String positionId;
    /**
     * 0 表示未提交 1 表示已提交
     */
    String hasReport;

    public String getHasReport() {
        return hasReport;
    }

    public void setHasReport(String hasReport) {
        this.hasReport = hasReport;
    }

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getReservoir() {
        return reservoir;
    }

    public void setReservoir(String reservoir) {
        this.reservoir = reservoir;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Override
    public synchronized boolean saveOrUpdate(String... conditions) {
        return super.saveOrUpdate(conditions);
    }

}
