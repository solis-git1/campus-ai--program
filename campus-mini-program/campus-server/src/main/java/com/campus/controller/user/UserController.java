package com.campus.controller.user;

import com.campus.dto.user.UserLoginDTO;
import com.campus.dto.user.UserRegisterDTO;
import com.campus.entity.User;
import com.campus.result.R;
import com.campus.service.UserService;
import com.campus.vo.UserLoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端用户管理控制器
 * 
 * <p>该类提供用户相关的API接口，包括用户登录、注册、信息查询和更新等功能，
 * 主要服务于微信小程序端的用户操作。</p>
 * 
 * @author campus
 * @since 1.0
 */
@RestController
@RequestMapping("/user/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 微信小程序登录接口
     * 
     * <p>该接口通过微信小程序提供的code进行用户身份验证，
     * 支持新用户自动注册和老用户直接登录。</p>
     * 
     * @param dto 登录参数，包含微信授权code
     * @return 登录结果，包含JWT token和用户基本信息
     */
    @PostMapping("/login")
    public R<UserLoginVO> login(@RequestBody @Valid UserLoginDTO dto) {
        log.info("用户微信登录：{}", dto);
        UserLoginVO vo = userService.wxLogin(dto);
        return R.success(vo);
    }

    /**
     * 用户注册接口（账号密码注册）
     * 
     * <p>该接口支持传统的用户名密码注册方式，
     * 会对用户名、密码格式进行校验，确保数据安全性。</p>
     * 
     * @param dto 注册参数，包含用户名、密码、昵称等信息
     * @return 注册成功结果
     */
    @PostMapping("/register")
    public R register(@RequestBody @Valid UserRegisterDTO dto) {
        log.info("用户注册：{}", dto);
        userService.register(dto);
        return R.success();
    }

    /**
     * 获取当前登录用户的信息
     * 
     * <p>该接口返回当前登录用户的详细信息，
     * 需要用户已登录且token有效。</p>
     * 
     * @return 当前用户的基本信息
     */
    @GetMapping("/info")
    public R<User> getUserInfo() {
        log.info("获取当前用户信息");
        User user = userService.getCurrentUser();
        return R.success(user);
    }

    /**
     * 更新用户信息
     * 
     * <p>该接口允许用户更新个人资料信息，
     * 如昵称、头像、联系方式等。</p>
     * 
     * @param user 用户信息更新对象
     * @return 更新成功结果
     */
    @PutMapping
    public R update(@RequestBody User user) {
        log.info("更新用户信息：{}", user);
        userService.updateUser(user);
        return R.success();
    }
}