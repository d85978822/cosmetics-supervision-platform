package com.cosmetics.system.service.impl;

import com.cosmetics.system.common.JwtUtils;
import com.cosmetics.system.common.RedisUtils;
import com.cosmetics.system.model.dto.CaptchaResponse;
import com.cosmetics.system.model.dto.LoginRequest;
import com.cosmetics.system.model.dto.LoginResponse;
import com.cosmetics.system.model.entity.SysPermission;
import com.cosmetics.system.model.entity.SysRole;
import com.cosmetics.system.model.entity.SysUser;
import com.cosmetics.system.service.AuthService;
import com.cosmetics.system.service.SysPermissionService;
import com.cosmetics.system.service.SysRoleService;
import com.cosmetics.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserService userService;
    private final SysRoleService roleService;
    private final SysPermissionService permissionService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";
    private static final String TOKEN_KEY_PREFIX = "token:";
    private static final long CAPTCHA_EXPIRE_TIME = 5;
    private static final long TOKEN_EXPIRE_TIME = 24;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 验证验证码
        if (request.getCaptcha() != null && request.getCaptchaKey() != null) {
            String captcha = (String) redisUtils.get(CAPTCHA_KEY_PREFIX + request.getCaptchaKey());
            if (captcha == null || !captcha.equalsIgnoreCase(request.getCaptcha())) {
                throw new BadCredentialsException("验证码错误");
            }
            redisUtils.delete(CAPTCHA_KEY_PREFIX + request.getCaptchaKey());
        }

        // 验证用户名和密码
        SysUser user = userService.getByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        if (user.getStatus() != 1) {
            throw new BadCredentialsException("账号已被禁用");
        }

        // 生成token
        String token = jwtUtils.generateToken(user.getUsername());
        redisUtils.set(TOKEN_KEY_PREFIX + token, user.getUsername(), TOKEN_EXPIRE_TIME, TimeUnit.HOURS);

        // 获取用户角色和权限
        List<SysRole> roles = roleService.getRolesByUserId(user.getId());
        List<SysPermission> permissions = permissionService.getPermissionsByUserId(user.getId());

        // 构建登录响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setAvatar(user.getAvatar());
        response.setRoles(roles.stream().map(SysRole::getRoleKey).collect(Collectors.toList()));
        response.setPermissions(permissions.stream().map(SysPermission::getPermissionKey).collect(Collectors.toList()));

        return response;
    }

    @Override
    public void logout(String token) {
        redisUtils.delete(TOKEN_KEY_PREFIX + token);
    }

    @Override
    public CaptchaResponse generateCaptcha() {
        String captchaKey = UUID.randomUUID().toString();
        String captchaCode = generateCaptchaCode();
        redisUtils.set(CAPTCHA_KEY_PREFIX + captchaKey, captchaCode, CAPTCHA_EXPIRE_TIME, TimeUnit.MINUTES);
        
        CaptchaResponse response = new CaptchaResponse();
        response.setKey(captchaKey);
        response.setImage(captchaCode); // 这里应该生成验证码图片的Base64字符串
        return response;
    }

    @Override
    public String refreshToken(String token) {
        String username = (String) redisUtils.get(TOKEN_KEY_PREFIX + token);
        if (username == null) {
            throw new BadCredentialsException("token已过期");
        }

        // 生成新token
        String newToken = jwtUtils.generateToken(username);
        redisUtils.set(TOKEN_KEY_PREFIX + newToken, username, TOKEN_EXPIRE_TIME, TimeUnit.HOURS);
        redisUtils.delete(TOKEN_KEY_PREFIX + token);

        return newToken;
    }

    private String generateCaptchaCode() {
        // 这里简单返回一个6位随机数，实际项目中应该使用图形验证码
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
} 