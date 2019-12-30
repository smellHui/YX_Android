package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import com.tepia.base.http.BaseResponse;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-07
 * Time            :       上午11:23
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :       水雨情
 **/
public class WaterPptnPictureBean extends BaseResponse {

    /**
     * data : {"limitWaterLevel":"135","sixHourDrp":"0","picPath":"","damElevation":"137.3","oneHourDrp":"0"}
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
         * limitWaterLevel : 135
         * sixHourDrp : 0
         * picPath :
         * damElevation : 137.3
         * oneHourDrp : 0
         */

        /**
         * 当前水位
         */
        private String rz;
        /**
         * 汛限水位
         */
        private String limitWaterLevel;
        private String sixHourDrp;
        /**
         * 最新图像
         */
        private String picPath;
        /**
         * 坝顶高程
         */
        private String damElevation;
        private String oneHourDrp;

        public String getRz() {
            return rz;
        }

        public void setRz(String rz) {
            this.rz = rz;
        }

        public String getLimitWaterLevel() {
            return limitWaterLevel;
        }

        public void setLimitWaterLevel(String limitWaterLevel) {
            this.limitWaterLevel = limitWaterLevel;
        }

        public String getSixHourDrp() {
            return sixHourDrp;
        }

        public void setSixHourDrp(String sixHourDrp) {
            this.sixHourDrp = sixHourDrp;
        }

        public String getPicPath() {
            return picPath;
        }

        public void setPicPath(String picPath) {
            this.picPath = picPath;
        }

        public String getDamElevation() {
            return damElevation;
        }

        public void setDamElevation(String damElevation) {
            this.damElevation = damElevation;
        }

        public String getOneHourDrp() {
            return oneHourDrp;
        }

        public void setOneHourDrp(String oneHourDrp) {
            this.oneHourDrp = oneHourDrp;
        }
    }
}
