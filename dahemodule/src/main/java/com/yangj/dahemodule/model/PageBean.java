package com.yangj.dahemodule.model;

import java.util.List;

/**
 * Author:xch
 * Date:2019/4/4
 * Do:
 */
public class PageBean<T> {

    private int total;
    private int size;
    private int current;
    private List<T> records;
    private int pages;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", records=" + records +
                ", pages=" + pages +
                '}';
    }
}
