package org.jleopard.pageHelper;

import java.util.List;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/10
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 *  分页信息
 */
public class PageInfo {

    private int page = 1; // 当前页

    public int totalPages = 0; // 总页数

    private int pageSize=2;// 每页2条数据

    private int totalRows = 0; // 总数据数

    private int pageStartRow = 0;// 每页的起始数

    private int pageEndRow = 0; // 每页显示数据的终止数

    private List<?> list;   //查出的信息


    public PageInfo(int totalRows, int pageSize) {
        init(totalRows, pageSize);// 通过对象记录总数划分
    }

    /** *//**
     * 初始化list，并告之该list每页的记录数
     * @param totalRows
     * @param pageSize
     */
    public void init(int totalRows, int pageSize) {
        this.pageSize = pageSize;
        this.totalRows = totalRows;
        if ((totalRows % pageSize) == 0) {
            totalPages = totalRows / pageSize;
        } else {
            totalPages = totalRows / pageSize + 1;
        }

        if (totalRows < pageSize) {
            this.pageStartRow = 0;
            this.pageEndRow = totalRows;
        } else {
            this.pageStartRow =pageSize*(this.getPage()-1);
            this.pageEndRow = (this.getPage()*pageSize);
        }
    }

    public String toString(int temp) {
        String str = Integer.toString(temp);
        return str;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    /**  get **/
    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public List<?> getList() {
        return list;
    }
}
