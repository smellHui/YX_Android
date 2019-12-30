package com.tepia.photo_picker.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-17
 * Time            :       下午9:34
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :       问题表现中的子项照片
 **/
public class CheckTaskPicturesBean extends DataSupport implements Serializable {

    String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    private String itemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public synchronized boolean saveOrUpdate(String... conditions) {
        return super.saveOrUpdate(conditions);
    }

    String bizKey;
    String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    String bizType;
    String filePath;

    /**
     * 标题
     */
    String nameTitle;

    private String reservoirId;

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }


    /**
     * 是否已核查该项任务，本地flag
     */
    private String  hasCheck;


    public String getHasCheck() {
        return hasCheck;
    }

    public void setHasCheck(String hasCheck) {
        this.hasCheck = hasCheck;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
