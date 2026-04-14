package com.campus.controller.admin;

import com.campus.context.BaseContext;
import com.campus.dto.admin.AdminLoginDTO;
import com.campus.entity.User;
import com.campus.mapper.UserMapper;
import com.campus.result.R;
import com.campus.service.AdminService;
import com.campus.vo.admin.AdminLoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public R<AdminLoginVO> login(@RequestBody @Valid AdminLoginDTO dto) {
        log.info("管理员登录：{}", dto.getUsername());
        AdminLoginVO vo = adminService.login(dto);
        return R.success(vo);
    }

    @GetMapping("/info")
    public R<User> getAdminInfo() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return R.error(401, "请先登录");
        }
        User user = userMapper.getById(userId);
        if (user == null) {
            return R.error(404, "用户不存在");
        }
        user.setPassword(null);
        return R.success(user);
    }
}
