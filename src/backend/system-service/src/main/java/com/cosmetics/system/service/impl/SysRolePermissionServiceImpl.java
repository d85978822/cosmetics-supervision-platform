package com.cosmetics.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cosmetics.system.mapper.SysRolePermissionMapper;
import com.cosmetics.system.model.entity.SysRolePermission;
import com.cosmetics.system.service.SysRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements SysRolePermissionService {

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除角色原有的权限关联
        deleteByRoleId(roleId);
        
        // 批量添加新的权限关联
        List<SysRolePermission> rolePermissions = permissionIds.stream()
            .map(permissionId -> {
                SysRolePermission rolePermission = new SysRolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                return rolePermission;
            })
            .collect(Collectors.toList());
        
        saveBatch(rolePermissions);
    }

    @Override
    public List<Long> getPermissionIds(Long roleId) {
        return list(
            new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId)
        ).stream()
            .map(SysRolePermission::getPermissionId)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByRoleId(Long roleId) {
        remove(
            new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId)
        );
    }

    @Override
    @Transactional
    public void deleteByPermissionId(Long permissionId) {
        remove(
            new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getPermissionId, permissionId)
        );
    }
} 