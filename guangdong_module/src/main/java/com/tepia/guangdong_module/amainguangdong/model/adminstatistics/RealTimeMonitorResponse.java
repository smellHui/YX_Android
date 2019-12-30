package com.tepia.guangdong_module.amainguangdong.model.adminstatistics;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    19:16
 * Describe : 行政统计首页-实时监测数据
 */
public class RealTimeMonitorResponse extends BaseResponse {

    private RealTimeMonitorBean data;

    public RealTimeMonitorBean getData() {
        return data;
    }

    public void setData(RealTimeMonitorBean data) {
        this.data = data;
    }
}
