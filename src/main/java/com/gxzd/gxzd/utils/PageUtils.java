package com.gxzd.gxzd.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * 分页数据封装类
 * Created by macro on 2019/4/19.
 */
@Data
public class PageUtils<T> {
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 分页数据
     */
    private List<T> list;

    public PageUtils() {
        this.pageNum = 0;
        this.pageSize = 20;
        this.totalPage = 0;
        this.total = 0L;
    }

    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T> PageUtils<T> restPage(List<T> list) {
        PageUtils<T> result = new PageUtils<T>();
        Page<T> pageInfo = new Page<>();
        pageInfo.setRecords(list);
        result.setTotalPage(Math.toIntExact(pageInfo.getPages()));
        result.setPageNum(Math.toIntExact(pageInfo.getCurrent()));
        result.setPageSize(Math.toIntExact(pageInfo.getSize()));
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getRecords());
        return result;
    }

    /**
     * 当分页对象和处理对象不一致的时候使用
     * @param list
     * @param list1
     * @param <T>
     * @return
     */
    public static <T> PageUtils<T> restPage(List list, Page<T> list1) {
        PageUtils<T> result = new PageUtils<T>();
        Page<T> pageInfo = new Page<>();
        result.setTotalPage(Math.toIntExact(pageInfo.getTotal()));
        result.setPageNum(Math.toIntExact(pageInfo.getPages()));
        result.setPageSize(Math.toIntExact(pageInfo.getSize()));
        result.setTotal(pageInfo.getTotal());
        result.setList(list1.getRecords());
        return result;
    }

    /**
     * 将SpringData分页后的list转为分页信息
     */
    public static <T> PageUtils<T> restPage(Page<T> pageInfo) {
        PageUtils<T> result = new PageUtils<T>();
        result.setTotalPage(Math.toIntExact(pageInfo.getCurrent()));
        result.setPageNum(Math.toIntExact(pageInfo.getPages()));
        result.setPageSize(Math.toIntExact(pageInfo.getSize()));
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getRecords());
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
