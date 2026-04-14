package com.campus.exception;

import com.campus.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器，统一处理应用中抛出的各种异常
 * 
 * <p>该类使用@RestControllerAdvice注解，拦截所有Controller层的异常，
 * 并返回统一的错误响应格式，提升API的友好性和安全性</p>
 * 
 * @author campus
 * @since 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * 
     * @param ex 业务异常对象
     * @return 统一错误响应
     */
    @ExceptionHandler(BaseException.class)
    public R<?> handleBaseException(BaseException ex) {
        log.warn("业务异常: code={}, msg={}", ex.getCode(), ex.getMessage());
        return R.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理参数校验异常（@Valid注解触发）
     * 
     * @param ex 参数校验异常
     * @return 统一错误响应，包含详细的校验失败信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleValidationException(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", msg);
        return R.error(400, "参数校验失败: " + msg);
    }

    /**
     * 处理参数绑定异常
     * 
     * @param ex 参数绑定异常
     * @return 统一错误响应
     */
    @ExceptionHandler(BindException.class)
    public R<?> handleBindException(BindException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("参数绑定失败: {}", msg);
        return R.error(400, "参数校验失败: " + msg);
    }

    /**
     * 处理缺少请求参数异常
     * 
     * @param ex 缺少请求参数异常
     * @return 统一错误响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<?> handleMissingParam(MissingServletRequestParameterException ex) {
        log.warn("缺少请求参数: {}", ex.getParameterName());
        return R.error(400, "缺少必要参数: " + ex.getParameterName());
    }

    /**
     * 处理参数类型不匹配异常
     * 
     * @param ex 参数类型不匹配异常
     * @return 统一错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("参数类型错误: name={}, value={}", ex.getName(), ex.getValue());
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "";
        return R.error(400, "参数「" + ex.getName() + "」格式不正确，期望类型: " + expected);
    }

    /**
     * 处理HTTP消息不可读异常（JSON解析失败等）
     * 
     * @param ex HTTP消息不可读异常
     * @return 统一错误响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        return R.error(400, "请求数据格式错误，请检查提交的数据是否正确");
    }

    /**
     * 处理文件上传大小超限异常
     * 
     * @param ex 文件上传大小超限异常
     * @return 统一错误响应
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        log.warn("文件上传大小超限");
        return R.error(400, "上传文件过大，请压缩后重试");
    }

    /**
     * 处理其他未捕获的异常
     * 
     * @param ex 异常对象
     * @return 统一错误响应，对数据库唯一约束异常进行特殊处理
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception ex) {
        log.error("系统异常", ex);
        String msg = ex.getMessage();
        if (msg != null && msg.contains("Duplicate entry")) {
            if (msg.contains("username")) {
                return R.error(409, "用户名已存在，请更换后重试");
            }
            if (msg.contains("phone")) {
                return R.error(409, "手机号已被注册");
            }
            return R.error(409, "数据已存在，请勿重复操作");
        }
        return R.error(500, "系统繁忙，请稍后重试");
    }
}
