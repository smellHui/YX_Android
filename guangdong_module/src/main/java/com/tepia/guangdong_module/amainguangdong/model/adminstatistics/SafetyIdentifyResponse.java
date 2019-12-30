package com.tepia.guangdong_module.amainguangdong.model.adminstatistics;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    19:30
 * Describe :行政统计首页-安全鉴定数据
 */
public class SafetyIdentifyResponse extends BaseResponse {

    private SafetyIdentifyBean data;

    public SafetyIdentifyBean getData() {
        return data;
    }

    public void setData(SafetyIdentifyBean data) {
        this.data = data;
    }
}
