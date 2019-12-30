package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-06-03
 * Time            :       下午5:58
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class AreaReservoirCountBean extends BaseResponse {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * monitorCount : 4
         * areaName : 天河区
         * reportCount : 0
         * allCount : 7
         */

        /**
         * 监测数量number
         */
        private int monitorCount;
        private String areaName;
        private int reportCount;
        private int allCount;

        public int getMonitorCount() {
            return monitorCount;
        }

        public void setMonitorCount(int monitorCount) {
            this.monitorCount = monitorCount;
        }

        public int getReportCount() {
            return reportCount;
        }

        public void setReportCount(int reportCount) {
            this.reportCount = reportCount;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }



        public int getAllCount() {
            return allCount;
        }

        public void setAllCount(int allCount) {
            this.allCount = allCount;
        }
    }
}
