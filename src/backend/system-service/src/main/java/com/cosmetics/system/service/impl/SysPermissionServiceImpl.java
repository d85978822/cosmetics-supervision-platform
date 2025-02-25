package com.cosmetics.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cosmetics.system.mapper.SysPermissionMapper;
import com.cosmetics.system.mapper.SysRolePermissionMapper;
import com.cosmetics.system.mapper.SysUserRoleMapper;
import com.cosmetics.system.model.entity.SysPermission;
import com.cosmetics.system.model.entity.SysRolePermission;
import com.cosmetics.system.model.entity.SysUserRole;
import com.cosmetics.system.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    private final SysUserRoleMapper userRoleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;

    @Override
    public List<SysPermission> getPermissionsByUserId(Long userId) {
        // 查询用户角色关联
        List<SysUserRole> userRoles = userRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
        
        if (userRoles.isEmpty()) {
            return List.of();
        }
        
        // 获取角色ID列表
        List<Long> roleIds = userRoles.stream()
            .map(SysUserRole::getRoleId)
            .collect(Collectors.toList());
        
        // 查询角色权限关联
        List<SysRolePermission> rolePermissions = rolePermissionMapper.selectList(
            new LambdaQueryWrapper<SysRolePermission>()
                .in(SysRolePermission::getRoleId, roleIds)
        );
        
        if (rolePermissions.isEmpty()) {
            return List.of();
        }
        
        // 获取权限ID列表
        List<Long> permissionIds = rolePermissions.stream()
            .map(SysRolePermission::getPermissionId)
            .distinct()
            .collect(Collectors.toList());
        
        // 查询权限信息
        return listByIds(permissionIds);
    }

    @Override
    public List<SysPermission> getPermissionsByRoleId(Long roleId) {
        // 查询角色权限关联
        List<SysRolePermission> rolePermissions = rolePermissionMapper.selectList(
            new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId)
        );
        
        if (rolePermissions.isEmpty()) {
            return List.of();
        }
        
        // 获取权限ID列表
        List<Long> permissionIds = rolePermissions.stream()
            .map(SysRolePermission::getPermissionId)
            .collect(Collectors.toList());
        
        // 查询权限信息
        return listByIds(permissionIds);
    }

    @Override
    public SysPermission getByPermissionKey(String permissionKey) {
        return getOne(
            new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getPermissionKey, permissionKey)
        );
    }

    @Override
    public List<SysPermission> getPermissionTree() {
        // 查询所有权限
        List<SysPermission> allPermissions = list(
            new LambdaQueryWrapper<SysPermission>()
                .orderByAsc(SysPermission::getSort)
        );
        
        // 构建权限树
        Map<Long, List<SysPermission>> permissionMap = allPermissions.stream()
            .collect(Collectors.groupingBy(SysPermission::getParentId));
        
        return buildPermissionTree(0L, permissionMap);
    }

    @Override
    @Transactional
    public void updateStatus(Long permissionId, Integer status) {
        SysPermission permission = new SysPermission();
        permission.setId(permissionId);
        permission.setStatus(status);
        updateById(permission);
    }

    private List<SysPermission> buildPermissionTree(Long parentId, Map<Long, List<SysPermission>> permissionMap) {
        List<SysPermission> permissions = permissionMap.get(parentId);
        if (permissions == null) {
            return new ArrayList<>();
        }
        
        for (SysPermission permission : permissions) {
            List<SysPermission> children = buildPermissionTree(permission.getId(), permissionMap);
            if (!children.isEmpty()) {
                permission.setChildren(children);
            }
        }
        
        return permissions;
    }
} 