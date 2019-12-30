package com.tepia.guangdong_module.amainguangdong.model.reservoirdetail;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/30
  * Version :1.0
  * 功能描述 :
 **/
public class PicDetailBean extends BaseResponse {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * stcd : GDSL0006
         * tm : 2019-05-10 00:00:00
         * picpath : http://119.1.151.132:3003/20171001/0000170505-01-03-171001000000.jpg
         */

        private String stcd;
        private String tm;
        private String picpath;

        public String getStcd() {
            return stcd;
        }

        public void setStcd(String stcd) {
            this.stcd = stcd;
        }

        public String getTm() {
            return tm;
        }

        public void setTm(String tm) {
            this.tm = tm;
        }

        public String getPicpath() {
            return picpath;
        }

        public void setPicpath(String picpath) {
            this.picpath = picpath;
        }
    }
}
