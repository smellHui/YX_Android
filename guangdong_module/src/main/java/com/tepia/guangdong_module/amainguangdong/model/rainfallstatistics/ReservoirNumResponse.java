package com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/17
 * Time :    10:42
 * Describe :
 */
public class ReservoirNumResponse extends BaseResponse{
    private ReservoirNumBean data;

    public ReservoirNumBean getData() {
        return data;
    }

    public void setData(ReservoirNumBean data) {
        this.data = data;
    }
}
