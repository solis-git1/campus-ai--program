package com.campus.result;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结果封装类
 * 
 * <p>该类用于统一API接口的响应格式，包含状态码、消息和数据三部分，
 * 提供成功和错误的静态工厂方法，简化响应构建过程</p>
 * 
 * @param <T> 响应数据的泛型类型
 * @author campus
 * @since 1.0
 */
@Data
public class R<T> implements Serializable {

    /**
     * 响应状态码：1表示成功，0或其他值表示失败
     */
    private Integer code;
    
    /**
     * 响应消息，用于描述操作结果
     */
    private String msg;
    
    /**
     * 响应数据，泛型类型，可为任意对象
     */
    private T data;

    /**
     * 创建成功的响应结果（无数据）
     * 
     * @param <T> 响应数据的泛型类型
     * @return 成功的响应对象
     */
    public static <T> R<T> success() {
        R<T> r = new R<>();
        r.code = 1;
        return r;
    }

    /**
     * 创建成功的响应结果（包含数据）
     * 
     * @param <T> 响应数据的泛型类型
     * @param object 响应数据对象
     * @return 成功的响应对象
     */
    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    /**
     * 创建错误的响应结果（使用默认错误码0）
     * 
     * @param <T> 响应数据的泛型类型
     * @param msg 错误消息
     * @return 错误的响应对象
     */
    public static <T> R<T> error(String msg) {
        return error(0, msg);
    }

    /**
     * 创建错误的响应结果（指定错误码）
     * 
     * @param <T> 响应数据的泛型类型
     * @param code 错误码
     * @param msg 错误消息
     * @return 错误的响应对象
     */
    public static <T> R<T> error(Integer code, String msg) {
        R<T> r = new R<>();
        r.code = code;
        r.msg = msg;
        return r;
    }
}