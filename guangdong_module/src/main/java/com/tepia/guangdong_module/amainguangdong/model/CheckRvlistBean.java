package com.tepia.guangdong_module.amainguangdong.model;

import com.tepia.base.http.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class CheckRvlistBean extends BaseResponse implements Serializable {

    /**
     * code : 0
     * count : 0
     * msg : null
     * data : {"pageNum":1,"pageSize":2,"size":2,"startRow":1,"endRow":2,"total":60,"pages":30,"list":[{"checkTaskId":"8d1e55ccd64b4cc1860cb9bae999a057","reservoirExpandId":"44625bcfd66714874a171sddf3f0013f","groupName":"D组","checkReservoirStatus":"0","id":"2c6e059141b3429aa7b74c468b55e515","reservoir":"天子坑水库","checkName":"2019年4月小型水库病险问题核查","checkDate":"201904"},{"checkTaskId":"8d1e55ccd64b4cc1860cb9bae999a057","reservoirExpandId":"44625bcfd66714874a171sddf3f0050f","groupName":"D组","checkReservoirStatus":"0","id":"ab757f1c91f64c1785453b3e8c3d0b91","reservoir":"佛坳水库","checkName":"2019年4月小型水库病险问题核查","checkDate":"201904"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * pageNum : 1
         * pageSize : 2
         * size : 2
         * startRow : 1
         * endRow : 2
         * total : 60
         * pages : 30
         * list : [{"checkTaskId":"8d1e55ccd64b4cc1860cb9bae999a057","reservoirExpandId":"44625bcfd66714874a171sddf3f0013f","groupName":"D组","checkReservoirStatus":"0","id":"2c6e059141b3429aa7b74c468b55e515","reservoir":"天子坑水库","checkName":"2019年4月小型水库病险问题核查","checkDate":"201904"},{"checkTaskId":"8d1e55ccd64b4cc1860cb9bae999a057","reservoirExpandId":"44625bcfd66714874a171sddf3f0050f","groupName":"D组","checkReservoirStatus":"0","id":"ab757f1c91f64c1785453b3e8c3d0b91","reservoir":"佛坳水库","checkName":"2019年4月小型水库病险问题核查","checkDate":"201904"}]
         */

        private int pageNum;
        private int pageSize;
        private int size;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private List<ListBean> list;

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

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
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

        public static class ListBean implements Serializable{
            /**
             * checkTaskId : 8d1e55ccd64b4cc1860cb9bae999a057
             * reservoirExpandId : 44625bcfd66714874a171sddf3f0013f
             * groupName : D组
             * checkReservoirStatus : 0
             * id : 2c6e059141b3429aa7b74c468b55e515
             * reservoir : 天子坑水库
             * checkName : 2019年4月小型水库病险问题核查
             * checkDate : 201904
             */

            private String checkTaskId;
            private String reservoirExpandId;
            private String groupName;
            private String checkReservoirStatus;
            private String id;
            private String reservoir;
            private String checkName;
            private String checkDate;

            public String getCheckTaskId() {
                return checkTaskId;
            }

            public void setCheckTaskId(String checkTaskId) {
                this.checkTaskId = checkTaskId;
            }

            public String getReservoirExpandId() {
                return reservoirExpandId;
            }

            public void setReservoirExpandId(String reservoirExpandId) {
                this.reservoirExpandId = reservoirExpandId;
            }

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public String getCheckReservoirStatus() {
                return checkReservoirStatus;
            }

            public void setCheckReservoirStatus(String checkReservoirStatus) {
                this.checkReservoirStatus = checkReservoirStatus;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getReservoir() {
                return reservoir;
            }

            public void setReservoir(String reservoir) {
                this.reservoir = reservoir;
            }

            public String getCheckName() {
                return checkName;
            }

            public void setCheckName(String checkName) {
                this.checkName = checkName;
            }

            public String getCheckDate() {
                return checkDate;
            }

            public void setCheckDate(String checkDate) {
                this.checkDate = checkDate;
            }
        }
    }
}
