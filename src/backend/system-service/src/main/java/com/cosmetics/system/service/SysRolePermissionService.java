package com.cosmetics.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmetics.system.model.entity.SysRolePermission;

import java.util.List;

public interface SysRolePermissionService extends IService<SysRolePermission> {
    
    /**
     * 分配角色权限
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 获取角色的权限ID列表
     */
    List<Long> getPermissionIds(Long roleId);
    
    /**
     * 删除角色的所有权限
     */
    void deleteByRoleId(Long roleId);
    
    /**
     * 删除权限的所有角色关联
     */
    void deleteByPermissionId(Long permissionId);
} 