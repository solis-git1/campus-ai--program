package com.campus.service;

import com.campus.dto.user.UserLoginDTO;
import com.campus.dto.user.UserRegisterDTO;
import com.campus.entity.User;
import com.campus.vo.UserLoginVO;

/**
 * 用户业务服务接口
 * 
 * <p>该接口定义了用户相关的核心业务逻辑，包括用户认证、注册、
 * 信息查询和更新等功能，为Controller层提供业务支持。</p>
 * 
 * @author campus
 * @since 1.0
 */
public interface UserService {

    /**
     * 微信小程序登录
     * 
     * <p>该方法通过微信小程序的授权code进行用户身份验证，
     * 支持新用户自动注册和老用户直接登录。</p>
     * 
     * @param dto 登录数据传输对象，包含微信授权code
     * @return 登录结果视图对象，包含JWT token和用户基本信息
     */
    UserLoginVO wxLogin(UserLoginDTO dto);

    /**
     * 用户注册
     * 
     * <p>该方法处理用户的注册请求，包括数据校验、密码加密、
     * 用户信息保存等操作。</p>
     * 
     * @param dto 注册数据传输对象，包含用户名、密码、昵称等信息
     */
    void register(UserRegisterDTO dto);

    /**
     * 获取当前登录用户的信息
     * 
     * <p>该方法从线程上下文中获取当前登录用户的ID，
     * 然后查询并返回用户的详细信息。</p>
     * 
     * @return 当前登录用户的基本信息
     */
    User getCurrentUser();

    /**
     * 更新用户信息
     * 
     * <p>该方法允许用户更新个人资料信息，
     * 如昵称、头像、联系方式等。</p>
     * 
     * @param user 用户信息更新对象
     */
    void updateUser(User user);
}