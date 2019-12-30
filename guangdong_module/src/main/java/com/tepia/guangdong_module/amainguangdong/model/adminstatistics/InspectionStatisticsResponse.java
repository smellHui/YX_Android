package com.tepia.guangdong_module.amainguangdong.model.adminstatistics;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    19:20
 * Describe :行政统计首页-巡查统计数据
 */
public class InspectionStatisticsResponse extends BaseResponse {
    private InspectionStatisticsBean data;

    public InspectionStatisticsBean getData() {
        return data;
    }

    public void setData(InspectionStatisticsBean data) {
        this.data = data;
    }
}
