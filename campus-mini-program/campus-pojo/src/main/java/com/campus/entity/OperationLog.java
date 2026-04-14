package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperationLog {
    private Long logId;
    private Long userId;
    private String username;
    private String operation; //操作类型：CREATE/UPDATE/DELETE/LOGIN等
    private String module; //模块：activity/course/user等
    private String description; //操作描述
    private String method; //请求方法
    private String url; //请求URL
    private String ip; //操作IP
    private LocalDateTime createTime;
}
