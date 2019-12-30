package com.yangj.dahemodule.model.patrol;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.common.base.Strings;
import com.yangj.dahemodule.common.HttpManager;

/**
 * Author:xch
 * Date:2019/12/23
 * Description:
 */
public class PatrolFlowBean implements MultiItemEntity {

    private String flowName;
    private int recordStatus;
    private String executeDate;
    private String resultType;
    private String resultDes;
    private String executorName;
    private boolean isXC = HttpManager.getInstance().isXC();

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultDes() {
        return Strings.nullToEmpty(resultDes);
    }

    public void setResultDes(String resultDes) {
        this.resultDes = resultDes;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public boolean isPass() {
        return resultType != null && resultType.equals("0");
    }

    @Override
    public String toString() {
        return "PatrolFlowBean{" +
                "flowName='" + flowName + '\'' +
                ", recordStatus='" + recordStatus + '\'' +
                ", executeDate='" + executeDate + '\'' +
                ", resultType=" + resultType +
                ", resultDes='" + resultDes + '\'' +
                ", executorName='" + executorName + '\'' +
                '}';
    }

    @Override
    public int getItemType() {
        if (recordStatus == 2) {
            if (isXC) return 6;
        }
        return recordStatus;
    }
}
