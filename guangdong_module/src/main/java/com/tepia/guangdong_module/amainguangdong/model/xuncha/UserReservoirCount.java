package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import com.tepia.base.http.BaseResponse;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-30
 * Time            :       下午8:15
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :行政统计-水库数量统计
 **/
public class UserReservoirCount extends BaseResponse {

    /**
     * data : {"smallTwoCount":1,"smallOneCount":1,"overLevelConut":0,"allCount":2}
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
         * smallTwoCount : 1
         * smallOneCount : 1
         * overLevelConut : 0
         * allCount : 2
         */

        /**
         * 小二型数量
         */
        private int smallTwoCount;
        /**
         * 小一型数量
         */
        private int smallOneCount;
        /**
         * 超汛限数量
         */
        private int overLevelConut;
        /**
         * 总数
         */
        private int allCount;

        private int threeDamCount;

        public int getSmallTwoCount() {
            return smallTwoCount;
        }

        public void setSmallTwoCount(int smallTwoCount) {
            this.smallTwoCount = smallTwoCount;
        }

        public int getSmallOneCount() {
            return smallOneCount;
        }

        public void setSmallOneCount(int smallOneCount) {
            this.smallOneCount = smallOneCount;
        }

        public int getOverLevelConut() {
            return overLevelConut;
        }

        public void setOverLevelConut(int overLevelConut) {
            this.overLevelConut = overLevelConut;
        }

        public int getAllCount() {
            return allCount;
        }

        public void setAllCount(int allCount) {
            this.allCount = allCount;
        }

        public int getThreeDamCount() {
            return threeDamCount;
        }

        public void setThreeDamCount(int threeDamCount) {
            this.threeDamCount = threeDamCount;
        }
    }
}
