package com.cosmetics.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cosmetics.system.mapper.SysUserRoleMapper;
import com.cosmetics.system.model.entity.SysUserRole;
import com.cosmetics.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        // 先删除用户原有的角色关联
        deleteByUserId(userId);
        
        // 批量添加新的角色关联
        List<SysUserRole> userRoles = roleIds.stream()
            .map(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRole;
            })
            .collect(Collectors.toList());
        
        saveBatch(userRoles);
    }

    @Override
    public List<Long> getRoleIds(Long userId) {
        return list(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        ).stream()
            .map(SysUserRole::getRoleId)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        remove(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
    }

    @Override
    @Transactional
    public void deleteByRoleId(Long roleId) {
        remove(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
        );
    }
} 