package com.tepia.guangdong_module.amainguangdong.model.reservoirregimen;

import com.tepia.base.http.BaseResponse;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/10
 * Time :    15:35
 * Describe :
 */
public class ReservoirRegimenResponse extends BaseResponse {

    private ReservoirRegimenPageBean data;

    public ReservoirRegimenPageBean getData() {
        return data;
    }

    public void setData(ReservoirRegimenPageBean data) {
        this.data = data;
    }
}
