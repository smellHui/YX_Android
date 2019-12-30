package com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/17
 * Time :    10:54
 * Describe :
 */
public class RainfallListResponse extends BaseResponse {


    private  RainfallPageBean data;

    public RainfallPageBean getData() {
        return data;
    }

    public void setData(RainfallPageBean data) {
        this.data = data;
    }
}
