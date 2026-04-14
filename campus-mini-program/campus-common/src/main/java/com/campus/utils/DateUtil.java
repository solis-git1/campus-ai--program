package com.campus.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类，统一处理时间的格式化与解析
 */
public class DateUtil {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将LocalDateTime格式化为默认格式的字符串
     * @param dateTime 待格式化的时间
     * @return 格式化后的字符串，null入参返回null
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * 将字符串解析为LocalDateTime
     * @param timeStr 待解析的时间字符串
     * @return 解析后的LocalDateTime，空入参返回null
     */
    public static LocalDateTime parse(String timeStr) {
        if (timeStr == null || timeStr.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(timeStr, DEFAULT_FORMATTER);
    }

    /**
     * 判断当前时间是否在指定的时间区间内
     * @param startTime 区间开始时间
     * @param endTime 区间结束时间
     * @return 是否在区间内
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }
}