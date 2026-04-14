package com.campus.context;

/**
 * 基于ThreadLocal封装的上下文工具类，用于在当前线程中保存和获取登录用户ID
 * 
 * <p>该类通过ThreadLocal实现线程隔离的用户上下文管理，适用于Web应用中的用户身份传递</p>
 * 
 * @author campus
 * @since 1.0
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的用户ID
     * 
     * @param id 用户ID，不能为null
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前线程的用户ID
     * 
     * @return 当前线程的用户ID，如果未设置则返回null
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 清除当前线程的用户ID
     * 
     * <p>在请求处理完成后调用，避免内存泄漏</p>
     */
    public static void remove() {
        threadLocal.remove();
    }
}