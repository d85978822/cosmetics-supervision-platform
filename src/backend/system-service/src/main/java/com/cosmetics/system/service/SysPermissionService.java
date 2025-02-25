package com.cosmetics.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmetics.system.model.entity.SysPermission;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {
    
    /**
     * 根据用户ID查询权限列表
     */
    List<SysPermission> getPermissionsByUserId(Long userId);
    
    /**
     * 根据角色ID查询权限列表
     */
    List<SysPermission> getPermissionsByRoleId(Long roleId);
    
    /**
     * 根据权限标识查询权限
     */
    SysPermission getByPermissionKey(String permissionKey);
    
    /**
     * 获取权限树
     */
    List<SysPermission> getPermissionTree();
    
    /**
     * 修改权限状态
     */
    void updateStatus(Long permissionId, Integer status);
} 