package com.campus.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础异常类，封装业务异常信息
 * 
 * <p>该类继承自RuntimeException，用于统一处理业务层异常，包含错误码和错误信息</p>
 * 
 * @author campus
 * @since 1.0
 */
@Getter
@Setter
public class BaseException extends RuntimeException {

    /**
     * 错误码，默认500表示服务器内部错误
     */
    private Integer code = 500;

    /**
     * 默认构造函数
     */
    public BaseException() {
    }

    /**
     * 使用错误信息构造异常
     * 
     * @param msg 错误信息
     */
    public BaseException(String msg) {
        super(msg);
    }

    /**
     * 使用错误码和错误信息构造异常
     * 
     * @param code 错误码
     * @param msg 错误信息
     */
    public BaseException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
