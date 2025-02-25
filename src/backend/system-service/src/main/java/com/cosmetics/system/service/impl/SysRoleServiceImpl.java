package com.cosmetics.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cosmetics.system.mapper.SysRoleMapper;
import com.cosmetics.system.mapper.SysUserRoleMapper;
import com.cosmetics.system.model.entity.SysRole;
import com.cosmetics.system.model.entity.SysUserRole;
import com.cosmetics.system.service.SysRolePermissionService;
import com.cosmetics.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysUserRoleMapper userRoleMapper;
    private final SysRolePermissionService rolePermissionService;

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        // 查询用户角色关联
        List<SysUserRole> userRoles = userRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
        
        // 获取角色ID列表
        List<Long> roleIds = userRoles.stream()
            .map(SysUserRole::getRoleId)
            .collect(Collectors.toList());
        
        // 查询角色信息
        return roleIds.isEmpty() ? List.of() : listByIds(roleIds);
    }

    @Override
    public SysRole getByRoleKey(String roleKey) {
        return getOne(
            new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleKey, roleKey)
        );
    }

    @Override
    @Transactional
    public void updateStatus(Long roleId, Integer status) {
        SysRole role = new SysRole();
        role.setId(roleId);
        role.setStatus(status);
        updateById(role);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionService.assignPermissions(roleId, permissionIds);
    }

    @Override
    public List<Long> getPermissionIds(Long roleId) {
        return rolePermissionService.getPermissionIds(roleId);
    }
} 