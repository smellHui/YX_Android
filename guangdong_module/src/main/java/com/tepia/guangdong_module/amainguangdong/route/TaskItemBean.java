package com.tepia.guangdong_module.amainguangdong.route;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tepia.base.view.floatview.CollectionsUtil;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by      Android studio
 *
 * @author :ly (from Center Of Wuhan)
 * 创建时间 :
 * 更新时间 :
 * Version :1.0
 * 功能描述 :
 **/

public class TaskItemBean extends DataSupport implements Serializable, MultiItemEntity {

    //步骤索引(0-第一次操作 1-正常 2-异常)
    private int stepIndex;

    @Override
    public int getItemType() {
        if (executeResultType == null) {
            stepIndex = 0;
        } else {
            stepIndex = isNormal() ? 1 : 2;
        }
        return stepIndex;
    }

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

    String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


    private String itemId;//巡查项id
    private String reservoirId;//水库

    private String workOrderId;//工单id
    private String positionId;//巡查点id
    private String positionTreeNames;//巡查点全级名称
    private String positionLongitude;//巡查点经度
    private String positionLatitude;
    private String superviseItemCode;//巡查项编码
    private String superviseItemName;//巡查项名称
    private String content;//巡查内容
    private String executeResultType;//执行结果类型
    private String executeResultDescription;//执行结果描述
    private String excuteLongitude;//执行点经度
    private String excuteLatitude;
    private String excuteDate;//执行日期
    private String lastExecuteResultDescription;//上次执行结果描述
    private String lastExcuteResultType;//上次执行结果类型（为“”或0，按钮正常异常，1/3：已正常、仍异常  2：正常、仍异常 ）

    //0 正常 1 异常 2 已正常 3仍异常

    private String operationLevel;//1：一般项 2：报警项

    private boolean isSelected;
    private String lastExcuteDate;
    private String planId;
    private String positionName;


    private List<ISysFileUploadsBean> iSysFileUploads;

    public List<ISysFileUploadsBean> getiSysFileUploads() {
        return iSysFileUploads;
    }

    public void setiSysFileUploads(List<ISysFileUploadsBean> iSysFileUploads) {
        this.iSysFileUploads = iSysFileUploads;
    }

    public static class ISysFileUploadsBean {
        /**
         * filePath : http://tepia-skgj-yxjg.oss-cn-beijing.aliyuncs.com/APP/patrolProblem/2019-05/tiny-71-2019-05-15-14-09-28.jpg
         */

        private String filePath;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    public String getOperationLevel() {
        return operationLevel;
    }

    public void setOperationLevel(String operationLevel) {
        this.operationLevel = operationLevel;
    }

    public String getLastExecuteResultDescription() {
        if (TextUtils.isEmpty(lastExecuteResultDescription)) {
            lastExecuteResultDescription = "";
        }
        return lastExecuteResultDescription;
    }

    public void setLastExecuteResultDescription(String lastExecuteResultDescription) {
        if (TextUtils.isEmpty(lastExecuteResultDescription)) {
            lastExecuteResultDescription = "";
        }
        this.lastExecuteResultDescription = lastExecuteResultDescription;
    }

    public String getLastExcuteResultType() {
        if (TextUtils.isEmpty(lastExcuteResultType)) {
            lastExcuteResultType = "";
        }
        return lastExcuteResultType;
    }

    public void setLastExcuteResultType(String lastExcuteResultType) {
        if (TextUtils.isEmpty(lastExcuteResultType)) {
            lastExcuteResultType = "";
        }
        this.lastExcuteResultType = lastExcuteResultType;
    }

    /**
     * 离线返回的字段对应
     */
    private String superviseItemContent;
    /**
     *
     */
    private String superviseItemResultType;
    private String resultType;
    /**
     * 工单完成情况 0 未完成 1 已完成
     */
    private String completeStatus;


    /**
     * 用于存储本地编辑的数据，本地是否已经提交
     */
    private String isCommitLocal;

    private String beforelist;

    public String getSuperviseItemContent() {
        return superviseItemContent;
    }

    public void setSuperviseItemContent(String superviseItemContent) {
        this.superviseItemContent = superviseItemContent;
    }


    public String getSuperviseItemResultType() {
        return superviseItemResultType;
    }

    public void setSuperviseItemResultType(String superviseItemResultType) {
        this.superviseItemResultType = superviseItemResultType;
    }

    public String getIsCommitLocal() {
        return isCommitLocal;
    }

    public void setIsCommitLocal(String isCommitLocal) {
        this.isCommitLocal = isCommitLocal;
    }


    public String getBeforelist() {
        return beforelist;
    }

    public void setBeforelist(String beforelist) {
        this.beforelist = beforelist;
    }

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private List<String> photoPaths;

    public void setPhotoPaths(List<String> photoPaths){
        this.photoPaths = photoPaths;
    }

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }


    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionTreeNames() {
        return positionTreeNames;
    }

    public void setPositionTreeNames(String positionTreeNames) {
        this.positionTreeNames = positionTreeNames;
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

    public String getSuperviseItemCode() {
        return superviseItemCode;
    }

    public void setSuperviseItemCode(String superviseItemCode) {
        this.superviseItemCode = superviseItemCode;
    }

    public String getSuperviseItemName() {
        return superviseItemName;
    }

    public void setSuperviseItemName(String superviseItemName) {
        this.superviseItemName = superviseItemName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }


    public String getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus;
    }

    public String getExecuteResultType() {
        return executeResultType;
    }

    public void setExecuteResultType(String executeResultType) {
        this.executeResultType = executeResultType;
    }

    public String getExecuteResultDescription() {
        return TextUtils.isEmpty(executeResultDescription) ? "" : executeResultDescription;
    }

    public void setExecuteResultDescription(String executeResultDescription) {
        this.executeResultDescription = executeResultDescription;
    }

    public String getExcuteLongitude() {
        return excuteLongitude;
    }

    public void setExcuteLongitude(String excuteLongitude) {
        this.excuteLongitude = excuteLongitude;
    }

    public String getExcuteLatitude() {
        return excuteLatitude;
    }

    public void setExcuteLatitude(String excuteLatitude) {
        this.excuteLatitude = excuteLatitude;
    }

    public String getExcuteDate() {
        return excuteDate;
    }

    public void setExcuteDate(String excuteDate) {
        this.excuteDate = excuteDate;
    }

    public String getLastExcuteDate() {
        return lastExcuteDate;
    }

    public void setLastExcuteDate(String lastExcuteDate) {
        this.lastExcuteDate = lastExcuteDate;
    }

    /**
     * 正常，异常
     *
     * @return
     */
    public boolean isNormal() {
        if (executeResultType != null) {
            return executeResultType.equals("") || executeResultType.equals("0") || executeResultType.equals("2");
        }
        return false;
    }

    /**
     * 是否已巡查
     *
     * @return
     */
    public boolean isCompleted() {
        return completeStatus != null && completeStatus.equals("1");
    }
}
