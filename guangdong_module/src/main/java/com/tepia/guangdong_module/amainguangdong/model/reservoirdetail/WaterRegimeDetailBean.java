package com.tepia.guangdong_module.amainguangdong.model.reservoirdetail;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/30
  * Version :1.0
  * 功能描述 : 水库水情详情
 **/
public class WaterRegimeDetailBean extends BaseResponse {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * floodSeasonWaterLevel : 224.05
         * rz : 15
         * w : 23.951
         * rrTime : 2019-05-10 07:15:00
         */

        private double floodSeasonWaterLevel;//汛限水位
        private double rz;
        private double w;
        private String rrTime;
        private double designSeasonWaterLevel;//设计洪水位
        private double checkSeasonWaterLevel;//校核洪水位

        public double getDesignSeasonWaterLevel() {
            return designSeasonWaterLevel;
        }

        public void setDesignSeasonWaterLevel(double designSeasonWaterLevel) {
            this.designSeasonWaterLevel = designSeasonWaterLevel;
        }

        public double getCheckSeasonWaterLevel() {
            return checkSeasonWaterLevel;
        }

        public void setCheckSeasonWaterLevel(double checkSeasonWaterLevel) {
            this.checkSeasonWaterLevel = checkSeasonWaterLevel;
        }

        public double getFloodSeasonWaterLevel() {
            return floodSeasonWaterLevel;
        }

        public void setFloodSeasonWaterLevel(double floodSeasonWaterLevel) {
            this.floodSeasonWaterLevel = floodSeasonWaterLevel;
        }

        public double getRz() {
            return rz;
        }

        public void setRz(double rz) {
            this.rz = rz;
        }

        public double getW() {
            return w;
        }

        public void setW(double w) {
            this.w = w;
        }

        public String getRrTime() {
            return rrTime;
        }

        public void setRrTime(String rrTime) {
            this.rrTime = rrTime;
        }
    }
}
