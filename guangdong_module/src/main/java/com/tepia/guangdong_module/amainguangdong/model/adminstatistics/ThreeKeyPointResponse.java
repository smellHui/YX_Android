package com.tepia.guangdong_module.amainguangdong.model.adminstatistics;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/3
 * Time :    19:28
 * Describe :行政统计首页-三个重点数据
 */
public class ThreeKeyPointResponse extends BaseResponse {

    private ThreeKeyPointBean data;

    public ThreeKeyPointBean getData() {
        return data;
    }

    public void setData(ThreeKeyPointBean data) {
        this.data = data;
    }
}
