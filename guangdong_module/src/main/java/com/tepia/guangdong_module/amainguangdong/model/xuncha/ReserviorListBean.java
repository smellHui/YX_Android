package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-30
 * Time            :       下午5:14
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class ReserviorListBean extends BaseResponse {

    /**
     * data : {"pageSize":10,"size":2,"total":2,"pages":1,"list":[{"reservoirId":"0001","reservoir":"水利大厦前","reservoirType":"3","damType":"0","floodSeasonWaterLevel":14.05,"areaName":"天河区","rz":0},{"reservoirId":"0002","reservoir":"水利大厦后","reservoirType":"2","damType":"0","floodSeasonWaterLevel":220,"areaName":"天河区","rz":0}]}
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
         * pageSize : 10
         * size : 2
         * total : 2
         * pages : 1
         * list : [{"reservoirId":"0001","reservoir":"水利大厦前","reservoirType":"3","damType":"0","floodSeasonWaterLevel":14.05,"areaName":"天河区","rz":0},{"reservoirId":"0002","reservoir":"水利大厦后","reservoirType":"2","damType":"0","floodSeasonWaterLevel":220,"areaName":"天河区","rz":0}]
         */

        private int pageSize;
        private int size;
        private int total;
        private int pages;
        private List<ListBean> list;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * reservoirId : 0001
             * reservoir : 水利大厦前
             * reservoirType : 3
             * damType : 0
             * floodSeasonWaterLevel : 14.05
             * areaName : 天河区
             * rz : 0
             */

            private String reservoirId;
            private String reservoir;
            private String reservoirType;
            private String damType;
            private double floodSeasonWaterLevel;
            private String areaName;
            private double rz;

            public String getReservoirId() {
                return reservoirId;
            }

            public void setReservoirId(String reservoirId) {
                this.reservoirId = reservoirId;
            }

            public String getReservoir() {
                return reservoir;
            }

            public void setReservoir(String reservoir) {
                this.reservoir = reservoir;
            }

            public String getReservoirType() {
                return reservoirType;
            }

            public void setReservoirType(String reservoirType) {
                this.reservoirType = reservoirType;
            }

            public String getDamType() {
                return damType;
            }

            public void setDamType(String damType) {
                this.damType = damType;
            }

            public double getFloodSeasonWaterLevel() {
                return floodSeasonWaterLevel;
            }

            public void setFloodSeasonWaterLevel(double floodSeasonWaterLevel) {
                this.floodSeasonWaterLevel = floodSeasonWaterLevel;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public double getRz() {
                return rz;
            }

            public void setRz(double rz) {
                this.rz = rz;
            }
        }
    }
}
