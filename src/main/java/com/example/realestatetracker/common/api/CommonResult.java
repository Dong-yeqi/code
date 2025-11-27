package com.example.realestatetracker.common.api;

import lombok.Data;

/**
 * 统一返回结果封装
 */
@Data
public class CommonResult<T> {

    private int code;
    private String message;
    private T data;

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> r = new CommonResult<>();
        r.setCode(200);
        r.setMessage("操作成功");
        r.setData(data);
        return r;
    }

    public static <T> CommonResult<T> failed(String message) {
        CommonResult<T> r = new CommonResult<>();
        r.setCode(500);
        r.setMessage(message);
        r.setData(null);
        return r;
    }

    public static <T> CommonResult<T> unauthorized(String message) {
        CommonResult<T> r = new CommonResult<>();
        r.setCode(401);
        r.setMessage(message);
        r.setData(null);
        return r;
    }
}
