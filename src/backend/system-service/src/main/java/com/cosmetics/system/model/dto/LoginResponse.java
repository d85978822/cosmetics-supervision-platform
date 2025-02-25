package com.cosmetics.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "登录响应结果")
public class LoginResponse {
    
    @Schema(description = "访问token")
    private String token;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "真实姓名")
    private String realName;
    
    @Schema(description = "头像")
    private String avatar;
    
    @Schema(description = "角色列表")
    private List<String> roles;
    
    @Schema(description = "权限列表")
    private List<String> permissions;
} 