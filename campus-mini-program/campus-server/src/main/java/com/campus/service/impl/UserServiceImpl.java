package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.context.BaseContext;
import com.campus.dto.user.UserLoginDTO;
import com.campus.dto.user.UserRegisterDTO;
import com.campus.entity.User;
import com.campus.entity.UserProfile;
import com.campus.exception.BaseException;
import com.campus.mapper.UserMapper;
import com.campus.mapper.UserProfileMapper;
import com.campus.properties.WeChatProperties;
import com.campus.service.UserService;
import com.campus.utils.JwtUtil;
import com.campus.utils.PasswordUtil;
import com.campus.vo.UserLoginVO;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 用户业务服务实现类
 * 
 * <p>该类实现了UserService接口，提供用户相关的核心业务逻辑实现，
 * 包括微信登录、用户注册、信息查询和更新等功能。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final WxMaService wxMaService;
    private final WeChatProperties weChatProperties;

    @Override
    public UserLoginVO wxLogin(UserLoginDTO dto) {
        if (dto.getCode() == null || dto.getCode().isBlank()) {
            throw new BaseException(400, "微信登录code不能为空");
        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(dto.getCode());
            String openid = session.getOpenid();
            log.info("微信用户openid:{}", openid);

            User user = userMapper.getByUsername(openid);
            if (user == null) {
                user = new User();
                user.setUsername(openid);
                user.setPassword(PasswordUtil.encode(Constant.DEFAULT_PASSWORD));
                user.setNickname("微信用户");
                user.setCreateTime(LocalDateTime.now());
                userMapper.insert(user);

                UserProfile profile = new UserProfile();
                profile.setUserId(user.getUserId());
                profile.setUpdateTime(LocalDateTime.now());
                userProfileMapper.insert(profile);
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            String token = JwtUtil.generateToken(claims);

            UserLoginVO vo = new UserLoginVO();
            vo.setUserId(user.getUserId());
            vo.setToken(token);
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
            return vo;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.error("微信登录失败", e);
            throw new BaseException(500, "登录失败，请稍后重试");
        }
    }

    @Override
    public void register(UserRegisterDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new BaseException(400, "用户名不能为空");
        }
        if (dto.getUsername().length() < 3 || dto.getUsername().length() > 20) {
            throw new BaseException(400, "用户名长度应为3-20个字符");
        }
        if (!Pattern.matches("^[a-zA-Z0-9_]+$", dto.getUsername())) {
            throw new BaseException(400, "用户名只能包含字母、数字和下划线");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BaseException(400, "密码不能为空");
        }
        if (dto.getPassword().length() < 6) {
            throw new BaseException(400, "密码长度不能少于6位");
        }
        if (dto.getPhone() != null && !dto.getPhone().isBlank()
                && !Pattern.matches("^1[3-9]\\d{9}$", dto.getPhone())) {
            throw new BaseException(400, "手机号格式不正确");
        }

        User exist = userMapper.getByUsername(dto.getUsername());
        if (exist != null) {
            throw new BaseException(409, "用户名「" + dto.getUsername() + "」已被注册，请更换后重试");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(PasswordUtil.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);

        UserProfile profile = new UserProfile();
        profile.setUserId(user.getUserId());
        profile.setUpdateTime(LocalDateTime.now());
        userProfileMapper.insert(profile);
    }

    @Override
    public User getCurrentUser() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BaseException(401, "请先登录");
        }
        User user = userMapper.getById(userId);
        if (user == null) {
            throw new BaseException(404, "当前用户不存在，请重新登录");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public void updateUser(User user) {
        Long userId = BaseContext.getCurrentId();
        user.setUserId(userId);
        user.setUsername(null);
        user.setPassword(null);
        user.setRole(null);
        user.setStatus(null);
        user.setCreateTime(null);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }
}