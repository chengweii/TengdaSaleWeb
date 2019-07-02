package com.common;

import lombok.Data;

/**
 * 描述信息
 *
 * @author chengwei11
 * @date 2019/7/2
 */
@Data
public class JsonResult<T> {
    private int code;
    private String message;
    private T result;

    public JsonResult(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T> JsonResult<T> success(T result) {
        return new JsonResult<T>(200, "success", result);
    }

    public static <T> JsonResult<T> fail(int code, String message, T result) {
        return new JsonResult<T>(code, message, result);
    }
}
