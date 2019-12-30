package com.tepia.guangdong_module.amainguangdong.model.adminstatistics;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    19:24
 * Describe :行政统计首页-三个责任人数据
 */
public class ThreePersonsResponse extends BaseResponse {

    private ThreePersonsBean data;

    public ThreePersonsBean getData() {
        return data;
    }

    public void setData(ThreePersonsBean data) {
        this.data = data;
    }
}
