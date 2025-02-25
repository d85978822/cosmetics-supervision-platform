package com.cosmetics.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmetics.system.model.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {
    
    /**
     * 分配用户角色
     */
    void assignRoles(Long userId, List<Long> roleIds);
    
    /**
     * 获取用户的角色ID列表
     */
    List<Long> getRoleIds(Long userId);
    
    /**
     * 删除用户的所有角色
     */
    void deleteByUserId(Long userId);
    
    /**
     * 删除角色的所有用户关联
     */
    void deleteByRoleId(Long roleId);
} 