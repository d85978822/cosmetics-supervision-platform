package com.cosmetics.system.service;

import com.cosmetics.system.model.dto.CaptchaResponse;
import com.cosmetics.system.model.dto.LoginRequest;
import com.cosmetics.system.model.dto.LoginResponse;

public interface AuthService {
    
    /**
     * 登录
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 登出
     */
    void logout(String token);
    
    /**
     * 生成验证码
     */
    CaptchaResponse generateCaptcha();
    
    /**
     * 刷新token
     */
    String refreshToken(String token);
} 