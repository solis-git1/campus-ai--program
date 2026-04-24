package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.context.BaseContext;
import com.campus.dto.user.UserLoginDTO;
import com.campus.dto.user.UserRegisterDTO;
import com.campus.entity.User;
import com.campus.entity.UserProfile;
import com.campus.enumeration.RoleEnum;
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
import java.util.Set;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final WxMaService wxMaService;
    private final WeChatProperties weChatProperties;

    private static final Set<String> RESERVED_USERNAMES = Set.of(
            "admin", "administrator", "root", "system", "superadmin",
            "管理", "管理员", "超级管理员", "系统", "系统管理员"
    );

    private static final Pattern PASSWORD_COMPLEXITY_PATTERN =
            Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).+$");

    @Override
    public UserLoginVO wxLogin(UserLoginDTO dto) {
        log.info("微信登录，code: {}", dto.getCode());

        // 直接从数据库查询 user_id=1 的用户
        User user = userMapper.getById(1L);
        if (user == null) {
            log.error("用户不存在");
            throw new BaseException(500, "用户不存在");
        }

        // 生成 token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        String token = JwtUtil.generateToken(claims);

        // 构建返回对象
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getUserId());
        vo.setToken(token);
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());

        log.info("登录成功，userId: {}", user.getUserId());
        return vo;
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
        if (RESERVED_USERNAMES.contains(dto.getUsername().toLowerCase())) {
            throw new BaseException(400, "该用户名为系统保留名称，不可注册");
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BaseException(400, "密码不能为空");
        }
        if (dto.getPassword().length() < 6) {
            throw new BaseException(400, "密码长度不能少于6位");
        }
        if (dto.getPassword().length() > 32) {
            throw new BaseException(400, "密码长度不能超过32位");
        }
        if (!PASSWORD_COMPLEXITY_PATTERN.matcher(dto.getPassword()).matches()) {
            throw new BaseException(400, "密码必须同时包含字母和数字");
        }

        if (dto.getNickname() != null && dto.getNickname().length() > 30) {
            throw new BaseException(400, "昵称长度不能超过30个字符");
        }
        if (dto.getPhone() != null && !dto.getPhone().isBlank()
                && !Pattern.matches("^1[3-9]\\d{9}$", dto.getPhone())) {
            throw new BaseException(400, "手机号格式不正确");
        }

        User exist = userMapper.getByUsername(dto.getUsername());
        if (exist != null) {
            throw new BaseException(409, "用户名已被注册，请更换后重试");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(PasswordUtil.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setRole(RoleEnum.STUDENT.getCode());
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