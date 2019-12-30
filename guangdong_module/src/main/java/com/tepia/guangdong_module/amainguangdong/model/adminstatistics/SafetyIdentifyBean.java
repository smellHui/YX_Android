package com.tepia.guangdong_module.amainguangdong.model.adminstatistics;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    19:31
 * Describe :
 */
public class SafetyIdentifyBean {
    private int allNoCount;
    private int allIdentifyCount;
    private int allCount;

    public int getAllNoCount() {
        return allNoCount;
    }

    public SafetyIdentifyBean setAllNoCount(int allNoCount) {
        this.allNoCount = allNoCount;
        return this;
    }

    public int getAllIdentifyCount() {
        return allIdentifyCount;
    }

    public SafetyIdentifyBean setAllIdentifyCount(int allIdentifyCount) {
        this.allIdentifyCount = allIdentifyCount;
        return this;
    }

    public int getAllCount() {
        return allCount;
    }

    public SafetyIdentifyBean setAllCount(int allCount) {
        this.allCount = allCount;
        return this;
    }
}
