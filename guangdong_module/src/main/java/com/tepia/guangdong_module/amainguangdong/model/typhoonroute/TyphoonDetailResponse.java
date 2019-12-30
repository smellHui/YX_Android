package com.tepia.guangdong_module.amainguangdong.model.typhoonroute;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
 * Created by Android Studio
 *
 * @author: Arthur
 * Date:    2019/5/31
 * Time:    10:44
 * Describe:
 */
public class TyphoonDetailResponse extends BaseResponse {

    private List<TyphoonRouteItemBean> data;

    public List<TyphoonRouteItemBean> getData() {
        return data;
    }

    public void setData(List<TyphoonRouteItemBean> data) {
        this.data = data;
    }
}
