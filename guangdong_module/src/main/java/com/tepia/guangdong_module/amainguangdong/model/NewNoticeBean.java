package com.tepia.guangdong_module.amainguangdong.model;

import com.tepia.base.http.BaseResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.SpecialNoticeBean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/9
  * Version :1.0
  * 功能描述 : 水库最新通知
 **/
public class NewNoticeBean extends BaseResponse implements Serializable {

    private List<SpecialNoticeBean> data;

    public List<SpecialNoticeBean> getData() {
        return data;
    }

    public void setData(List<SpecialNoticeBean> data) {
        this.data = data;
    }

}
