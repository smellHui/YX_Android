package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-09
 * Time            :       下午7:33
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class SpecialNoticeBean extends DataSupport {
    /**
     * id : a84a912356c94d33b5921d2c87384a68
     * noticeTitle : 巡查通知啊
     * noticeType : 3
     * noticeContent : 天气预报有大暴雨，注意水位
     * noticeStatus : 0
     * createBy : 501bb19b45d44caf9807d6ba55db27ae
     * createDate : 2019-05-08 10:12:06
     * userName : 超级管理员
     * roleName : 超级管理员
     */

    @SerializedName("id")
    private String onlyId;
    private String noticeTitle;
    private String noticeType;
    private String noticeContent;
    private String noticeStatus;
    private String createBy;
    private String createDate;
    private String userName;
    private String roleName;
    private String completeStatus;
    private String workOrderId;
    String reservoirId;

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

    public String getId() {
        return onlyId;
    }

    public void setId(String id) {
        this.onlyId = id;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus;
    }
}
