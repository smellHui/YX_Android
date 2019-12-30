package com.tepia.guangdong_module.amainguangdong.route;

import com.google.gson.annotations.SerializedName;
import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-05-16
 * Time            :       下午4:41
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class TaskBeanFromNet extends BaseResponse {

    /**
     * data : {"pageNum":1,"pageSize":1,"size":1,"total":18,"list":[{"workOrderId":"02c7599a00d443f3bd410297479c3b1f","workOrderName":"水利大厦前20190513巡检工单","createDate":"2019-05-13 17:54:39","executorName":"水利巡查","reservoirName":"水利大厦前","routeType":"3","routeName":"特别巡查"}]}
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
         * pageNum : 1
         * pageSize : 1
         * size : 1
         * total : 18
         * list : [{"workOrderId":"02c7599a00d443f3bd410297479c3b1f","workOrderName":"水利大厦前20190513巡检工单","createDate":"2019-05-13 17:54:39","executorName":"水利巡查","reservoirName":"水利大厦前","routeType":"3","routeName":"特别巡查"}]
         */
        private int pageNum;
        private int pageSize;
        private int size;
        private int total;
        private int pages;

        @SerializedName("list")
        List<TaskBean> list;


        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

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

        public List<TaskBean> getList() {
            return list;
        }

        public void setList(List<TaskBean> list) {
            this.list = list;
        }

        /*public static class ListBean {
            *//**
             * workOrderId : 02c7599a00d443f3bd410297479c3b1f
             * workOrderName : 水利大厦前20190513巡检工单
             * createDate : 2019-05-13 17:54:39
             * executorName : 水利巡查
             * reservoirName : 水利大厦前
             * routeType : 3
             * routeName : 特别巡查
             *//*

            private String workOrderId;
            private String workOrderName;
            private String createDate;
            private String executorName;
            private String reservoirName;
            private String routeType;
            private String routeName;

            public String getWorkOrderId() {
                return workOrderId;
            }

            public void setWorkOrderId(String workOrderId) {
                this.workOrderId = workOrderId;
            }

            public String getWorkOrderName() {
                return workOrderName;
            }

            public void setWorkOrderName(String workOrderName) {
                this.workOrderName = workOrderName;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getExecutorName() {
                return executorName;
            }

            public void setExecutorName(String executorName) {
                this.executorName = executorName;
            }

            public String getReservoirName() {
                return reservoirName;
            }

            public void setReservoirName(String reservoirName) {
                this.reservoirName = reservoirName;
            }

            public String getRouteType() {
                return routeType;
            }

            public void setRouteType(String routeType) {
                this.routeType = routeType;
            }

            public String getRouteName() {
                return routeName;
            }

            public void setRouteName(String routeName) {
                this.routeName = routeName;
            }
        }*/
    }
}
