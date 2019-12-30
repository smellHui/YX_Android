package com.tepia.guangdong_module.amainguangdong.model.typhoonroute;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date:    2019/5/31
 * Time:    10:37
 * Describe:
 */
public class TyphoonListResponse extends BaseResponse {

    private List<TyphoonListItemBean> data;

    public List<TyphoonListItemBean> getData() {
        return data;
    }

    public void setData(List<TyphoonListItemBean> data) {
        this.data = data;
    }
}
