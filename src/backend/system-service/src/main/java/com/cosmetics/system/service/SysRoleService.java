package com.cosmetics.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmetics.system.model.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    
    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> getRolesByUserId(Long userId);
    
    /**
     * 根据角色标识查询角色
     */
    SysRole getByRoleKey(String roleKey);
    
    /**
     * 修改角色状态
     */
    void updateStatus(Long roleId, Integer status);
    
    /**
     * 分配角色权限
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 获取角色的权限ID列表
     */
    List<Long> getPermissionIds(Long roleId);
} 