package com.cosmetics.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmetics.system.model.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    
    /**
     * 根据用户名查询用户
     */
    SysUser getByUsername(String username);
    
    /**
     * 注册用户
     */
    void register(SysUser user);
    
    /**
     * 修改密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 重置密码
     */
    void resetPassword(Long userId);
    
    /**
     * 修改用户状态
     */
    void updateStatus(Long userId, Integer status);
} 