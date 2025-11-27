package com.example.realestatetracker.common.api;

import lombok.Data;

import java.util.List;

/**
 * 分页返回结果
 */
@Data
public class PageResult<T> {

    private long total;
    private List<T> list;

    public static <T> PageResult<T> of(long total, List<T> list) {
        PageResult<T> r = new PageResult<>();
        r.setTotal(total);
        r.setList(list);
        return r;
    }
}
