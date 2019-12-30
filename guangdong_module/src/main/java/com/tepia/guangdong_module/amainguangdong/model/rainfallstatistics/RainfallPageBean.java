package com.tepia.guangdong_module.amainguangdong.model.rainfallstatistics;

import java.util.List;

/**
 * Created by Android Studio
 *
 * @author : Arthur
 * Date :    2019/6/17
 * Time :    20:52
 * Describe :
 */
public class RainfallPageBean {
    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;

    private List<RainfallItemBean> list;

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

    public List<RainfallItemBean> getList() {
        return list;
    }

    public void setList(List<RainfallItemBean> list) {
        this.list = list;
    }
}
