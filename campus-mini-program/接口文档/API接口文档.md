# 校园助手小程序 API 接口文档

## 📋 文档说明

### 项目概述
校园助手小程序是一个面向高校师生的综合性服务平台，提供课程管理、活动参与、教室查询、待办任务、AI助手等核心功能。

### 接口统计
- **用户端接口**：29 个
- **管理员端接口**：43 个
- **总计接口**：72 个

### 接口规范
- **请求方式**：GET、POST、PUT、DELETE
- **路径格式**：类路径前缀 + 方法路径
- **认证方式**：JWT Token（用户端）/ Session（管理端）
- **数据格式**：JSON

### 基础路径
- **开发环境**：`http://localhost:8080`
- **生产环境**：`https://api.campus.example.com`

---

## 1. 用户端接口

### 1.1 用户管理接口 (UserController)
**基础路径：** `/user/user`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| POST | `/user/user/login` | 微信小程序登录接口 |
| POST | `/user/user/register` | 用户注册接口（账号密码注册） |
| GET | `/user/user/info` | 获取当前登录用户的信息 |
| PUT | `/user/user` | 更新用户信息 |

### 1.2 课程/课表接口 (CourseController)
**基础路径：** `/user/course`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/user/course/my` | 获取我的课表（含状态标记：current/completed） |
| GET | `/user/course/{courseId}` | 获取课程详情 |
| POST | `/user/course/add/{courseId}` | 添加课程到我的课表 |
| DELETE | `/user/course/remove/{courseId}` | 从我的课表删除课程 |
| POST | `/user/course/reminder/{courseId}` | 设置课程提醒 |

### 1.3 校园活动接口 (ActivityController)
**基础路径：** `/user/activity`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/user/activity/list` | 分页查询活动列表（支持多维度筛选） |
| GET | `/user/activity/{activityId}` | 获取活动详情 |
| GET | `/user/activity/hot` | 获取热门活动推荐 |
| POST | `/user/activity/register/{activityId}` | 活动报名 |
| POST | `/user/activity/favorite/{activityId}` | 收藏活动 |

### 1.4 教室管理接口 (ClassroomController)
**基础路径：** `/user/classroom`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/user/classroom/list` | 查询所有教室 |
| GET | `/user/classroom/empty` | 查询空教室 |
| GET | `/user/classroom/empty/filter` | 多条件筛选空教室（含推荐指数） |
| POST | `/user/classroom/favorite/{classroomId}` | 收藏教室 |
| DELETE | `/user/classroom/favorite/{classroomId}` | 取消收藏教室 |

### 1.5 待办任务接口 (TaskController)
**基础路径：** `/user/task`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| POST | `/user/task` | 新增待办任务 |
| GET | `/user/task/list` | 分页查询待办列表 |
| PUT | `/user/task` | 更新待办任务 |
| POST | `/user/task/finish/{taskId}` | 完成待办任务 |

### 1.6 AI智能推荐接口 (RecommendationController)
**基础路径：** `/user/recommendation`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/user/recommendation/my` | 获取我的推荐列表 |
| POST | `/user/recommendation/click/{recId}` | 标记推荐为已点击 |

### 1.7 系统通知接口 (NotificationController)
**基础路径：** `/user/notification`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/user/notification/my` | 获取我的通知列表 |
| POST | `/user/notification/read/{notificationId}` | 标记通知为已读 |

### 1.8 AI助手接口 (AiController)
**基础路径：** `/user/ai`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| POST | `/user/ai/chat` | 与AI助手对话 |
| GET | `/user/ai/history` | 获取对话历史 |

---

## 2. 管理员端接口

### 2.1 管理员管理接口 (AdminController)
**基础路径：** `/admin/admin`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| POST | `/admin/admin/login` | 管理员登录 |
| GET | `/admin/admin/info` | 获取管理员信息 |

### 2.2 用户管理接口 (AdminUserController)
**基础路径：** `/admin/user`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/user/list` | 分页查询用户列表 |
| GET | `/admin/user/{userId}` | 查询用户详情 |
| PUT | `/admin/user/status` | 更新用户状态（启用/禁用） |
| PUT | `/admin/user/{userId}/reset-password` | 重置用户密码 |
| GET | `/admin/user/statistics` | 获取用户统计数据 |

### 2.3 活动管理接口 (AdminActivityController)
**基础路径：** `/admin/activity`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/activity/list` | 分页查询活动列表 |
| GET | `/admin/activity/{activityId}` | 查询活动详情 |
| POST | `/admin/activity` | 创建活动 |
| PUT | `/admin/activity` | 更新活动 |
| DELETE | `/admin/activity/{activityId}` | 删除活动 |

### 2.4 课程管理接口 (AdminCourseController)
**基础路径：** `/admin/course`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/course/list` | 分页查询课程列表 |
| GET | `/admin/course/{courseId}` | 查询课程详情 |
| POST | `/admin/course` | 创建课程 |
| PUT | `/admin/course` | 更新课程 |
| DELETE | `/admin/course/{courseId}` | 删除课程 |

### 2.5 教室管理接口 (AdminClassroomController)
**基础路径：** `/admin/classroom`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/classroom/list` | 分页查询教室列表 |
| GET | `/admin/classroom/{classroomId}` | 查询教室详情 |
| POST | `/admin/classroom` | 创建教室 |
| PUT | `/admin/classroom` | 更新教室 |
| DELETE | `/admin/classroom/{classroomId}` | 删除教室 |

### 2.6 数据统计看板接口 (AdminDashboardController)
**基础路径：** `/admin/dashboard`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/dashboard` | 获取完整的数据看板 |
| GET | `/admin/dashboard/behavior-stats` | 获取用户行为统计 |
| GET | `/admin/dashboard/feature-usage` | 获取功能使用统计 |

### 2.7 推荐管理接口 (AdminRecommendationController)
**基础路径：** `/admin/recommendation`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/recommendation/list` | 查询推荐记录 |
| GET | `/admin/recommendation/configs` | 获取推荐配置列表 |
| PUT | `/admin/recommendation/configs` | 更新推荐配置 |

### 2.8 AI会话管理接口 (AdminAiController)
**基础路径：** `/admin/ai`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/ai/sessions/list` | 查询AI会话列表 |
| GET | `/admin/ai/sessions/{sessionId}/messages` | 查询会话消息列表 |
| GET | `/admin/ai/feedbacks/list` | 查询反馈列表 |

### 2.9 知识库管理接口 (AdminKnowledgeController)
**基础路径：** `/admin/knowledge`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/knowledge/documents/list` | 分页查询文档列表 |
| GET | `/admin/knowledge/documents/{docId}` | 查询文档详情 |
| POST | `/admin/knowledge/documents/upload` | 上传文档 |

### 2.10 通知管理接口 (AdminNotificationController)
**基础路径：** `/admin/notification`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/notification/list` | 分页查询通知列表 |
| GET | `/admin/notification/{notificationId}` | 查询通知详情 |
| POST | `/admin/notification` | 创建通知 |

### 2.11 日志管理接口 (AdminLogController)
**基础路径：** `/admin/log`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/log/list` | 查询操作日志列表 |
| GET | `/admin/log/statistics` | 获取操作日志统计 |

### 2.12 系统管理接口 (AdminSystemController)
**基础路径：** `/admin/system`

| 请求方式 | 接口路径 | 功能描述 |
|---------|---------|---------|
| GET | `/admin/system/info` | 获取系统信息 |
| GET | `/admin/system/health` | 系统健康检查 |
| PUT | `/admin/system/cache/clear` | 清理系统缓存 |

---

## 3. 接口详细说明

### 3.1 通用响应格式
```json
{
    "code": 200,
    "message": "success",
    "data": {}
}
```

### 3.2 错误码说明
- **200**: 成功
- **400**: 请求参数错误
- **401**: 未授权/登录过期
- **403**: 权限不足
- **404**: 资源不存在
- **500**: 服务器内部错误

### 3.3 认证方式
- **用户端**: JWT Token 认证，Token 放在请求头 `Authorization: Bearer {token}`
- **管理员端**: Session 认证，需要登录后使用

### 3.4 分页参数
- **page**: 页码（默认1）
- **pageSize**: 每页大小（默认10）

---

## 4. 接口统计汇总

### 4.1 用户端接口统计
| 模块 | 接口数量 |
|------|---------|
| 用户管理 | 4 |
| 课程/课表 | 5 |
| 校园活动 | 5 |
| 教室管理 | 5 |
| 待办任务 | 4 |
| AI智能推荐 | 2 |
| 系统通知 | 2 |
| AI助手 | 2 |
| **总计** | **29** |

### 4.2 管理员端接口统计
| 模块 | 接口数量 |
|------|---------|
| 管理员管理 | 2 |
| 用户管理 | 5 |
| 活动管理 | 5 |
| 课程管理 | 5 |
| 教室管理 | 5 |
| 数据统计看板 | 3 |
| 推荐管理 | 3 |
| AI会话管理 | 3 |
| 知识库管理 | 3 |
| 通知管理 | 3 |
| 日志管理 | 2 |
| 系统管理 | 3 |
| **总计** | **43** |

### 4.3 总体统计
- **用户端接口**: 29 个
- **管理员端接口**: 43 个
- **总计接口**: 72 个

---

## 5. 更新记录

| 版本 | 更新日期 | 更新内容 |
|------|---------|---------|
| v1.0 | 2024-01-01 | 初始版本，包含所有72个接口 |

---

## 6. 联系方式

如有接口相关问题，请联系：
- **技术负责人**: 张三
- **邮箱**: tech@campus.example.com
- **电话**: 138-0000-0000

---

*文档最后更新：2024年1月1日*