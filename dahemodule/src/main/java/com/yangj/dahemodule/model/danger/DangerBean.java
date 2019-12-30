package com.yangj.dahemodule.model.danger;

/**
 * Author:xch
 * Date:2019/10/15
 * Description:险情处理
 */
public class DangerBean {
    private String status;
    private String createBy;
    private String creatorName;
    private String createTime;
    private String updateBy;
    private String updaterName;
    private String updateTime;
    private String id;
    private String reservoirCode;
    private String source;
    private String title;
    private String description;
    private String reportUserId;
    private String sourceId;
    private String problemStatus;
    private String feedback;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
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

    public String getReservoirCode() {
        return reservoirCode;
    }

    public void setReservoirCode(String reservoirCode) {
        this.reservoirCode = reservoirCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportUserId() {
        return reportUserId;
    }

    public void setReportUserId(String reportUserId) {
        this.reportUserId = reportUserId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getProblemStatus() {
        return problemStatus;
    }

    public void setProblemStatus(String problemStatus) {
        this.problemStatus = problemStatus;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "DangerBean{" +
                "status='" + status + '\'' +
                ", createBy='" + createBy + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id='" + id + '\'' +
                ", reservoirCode='" + reservoirCode + '\'' +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", reportUserId='" + reportUserId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", problemStatus='" + problemStatus + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
