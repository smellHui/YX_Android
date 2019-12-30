package com.yangj.dahemodule.model.patrol;

import java.util.List;

/**
 * Author:xch
 * Date:2019/12/23
 * Description:
 */
public class PatrolRecordBean {

    private String creatorName;
    private int executeStatus;
    private String createTime;
    private String recordName;
    private List<PatrolFlowBean> flowList;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(int executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public List<PatrolFlowBean> getFlowList() {
        return flowList;
    }

    public void setFlowList(List<PatrolFlowBean> flowList) {
        this.flowList = flowList;
    }

    @Override
    public String toString() {
        return "PatrolRecordBean{" +
                "creatorName='" + creatorName + '\'' +
                ", executeStatus=" + executeStatus +
                ", createTime='" + createTime + '\'' +
                ", recordName='" + recordName + '\'' +
                ", flowList=" + flowList +
                '}';
    }
}
