package com.tepia.guangdong_module.amainguangdong.model.reservoirdetail;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/29
  * Version :1.0
  * 功能描述 : 雨量详情
 **/
public class RainDetailBean extends BaseResponse {

    /**
     * data : {"stcd":"GDSL0001","stnm":"水利大厦前雨量站","lgtd":"113.339383","lttd":"23.151675","stlc":"","addvcd":"440106","sttp":"PP","admauth":"","stPptnRs":[{"stcd":"GDSL0001","tm":"2019-05-01 00:00","drp":0,"tmStr":"2019-05-01 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-02 00:00","drp":0,"tmStr":"2019-05-02 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-03 00:00","drp":0,"tmStr":"2019-05-03 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-04 00:00","drp":0,"tmStr":"2019-05-04 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-05 00:00","drp":0,"tmStr":"2019-05-05 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-06 00:00","drp":0,"tmStr":"2019-05-06 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-07 00:00","drp":0,"tmStr":"2019-05-07 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-08 00:00","drp":0,"tmStr":"2019-05-08 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-09 00:00","drp":10,"tmStr":"2019-05-09 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-10 00:00","drp":6.8,"tmStr":"2019-05-10 00:00:00"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * stcd : GDSL0001
         * stnm : 水利大厦前雨量站
         * lgtd : 113.339383
         * lttd : 23.151675
         * stlc :
         * addvcd : 440106
         * sttp : PP
         * admauth :
         * stPptnRs : [{"stcd":"GDSL0001","tm":"2019-05-01 00:00","drp":0,"tmStr":"2019-05-01 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-02 00:00","drp":0,"tmStr":"2019-05-02 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-03 00:00","drp":0,"tmStr":"2019-05-03 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-04 00:00","drp":0,"tmStr":"2019-05-04 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-05 00:00","drp":0,"tmStr":"2019-05-05 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-06 00:00","drp":0,"tmStr":"2019-05-06 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-07 00:00","drp":0,"tmStr":"2019-05-07 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-08 00:00","drp":0,"tmStr":"2019-05-08 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-09 00:00","drp":10,"tmStr":"2019-05-09 00:00:00"},{"stcd":"GDSL0001","tm":"2019-05-10 00:00","drp":6.8,"tmStr":"2019-05-10 00:00:00"}]
         */

        private String stcd;
        private String stnm;
        private String lgtd;
        private String lttd;
        private String stlc;
        private String addvcd;
        private String sttp;
        private String admauth;
        private List<StPptnRsBean> stPptnRs;

        public String getStcd() {
            return stcd;
        }

        public void setStcd(String stcd) {
            this.stcd = stcd;
        }

        public String getStnm() {
            return stnm;
        }

        public void setStnm(String stnm) {
            this.stnm = stnm;
        }

        public String getLgtd() {
            return lgtd;
        }

        public void setLgtd(String lgtd) {
            this.lgtd = lgtd;
        }

        public String getLttd() {
            return lttd;
        }

        public void setLttd(String lttd) {
            this.lttd = lttd;
        }

        public String getStlc() {
            return stlc;
        }

        public void setStlc(String stlc) {
            this.stlc = stlc;
        }

        public String getAddvcd() {
            return addvcd;
        }

        public void setAddvcd(String addvcd) {
            this.addvcd = addvcd;
        }

        public String getSttp() {
            return sttp;
        }

        public void setSttp(String sttp) {
            this.sttp = sttp;
        }

        public String getAdmauth() {
            return admauth;
        }

        public void setAdmauth(String admauth) {
            this.admauth = admauth;
        }

        public List<StPptnRsBean> getStPptnRs() {
            return stPptnRs;
        }

        public void setStPptnRs(List<StPptnRsBean> stPptnRs) {
            this.stPptnRs = stPptnRs;
        }

        public static class StPptnRsBean {
            /**
             * stcd : GDSL0001
             * tm : 2019-05-01 00:00
             * drp : 0
             * tmStr : 2019-05-01 00:00:00
             */

            private String stcd;
            private String tm;
            private double drp;
            private String tmStr;

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

            public double getDrp() {
                return drp;
            }

            public void setDrp(double drp) {
                this.drp = drp;
            }

            public String getTmStr() {
                return tmStr;
            }

            public void setTmStr(String tmStr) {
                this.tmStr = tmStr;
            }
        }
    }
}
