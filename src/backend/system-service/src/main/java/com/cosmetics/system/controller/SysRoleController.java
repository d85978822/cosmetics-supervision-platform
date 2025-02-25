package com.cosmetics.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cosmetics.system.common.PageQuery;
import com.cosmetics.system.common.Result;
import com.cosmetics.system.model.entity.SysRole;
import com.cosmetics.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "分页查询角色列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<Page<SysRole>> page(PageQuery pageQuery, SysRole role) {
        Page<SysRole> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
            .like(role.getRoleName() != null, SysRole::getRoleName, role.getRoleName())
            .like(role.getRoleKey() != null, SysRole::getRoleKey, role.getRoleKey())
            .eq(role.getStatus() != null, SysRole::getStatus, role.getStatus())
            .orderByAsc(SysRole::getRoleSort);
        
        return Result.success(roleService.page(page, wrapper));
    }

    @Operation(summary = "获取角色详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<SysRole> getInfo(@Parameter(description = "角色ID") @PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add')")
    public Result<Void> add(@RequestBody SysRole role) {
        roleService.save(role);
        return Result.success();
    }

    @Operation(summary = "修改角色")
    @PutMapping
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Void> edit(@RequestBody SysRole role) {
        roleService.updateById(role);
        return Result.success();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public Result<Void> remove(@Parameter(description = "角色ID串") @PathVariable List<Long> ids) {
        roleService.removeByIds(ids);
        return Result.success();
    }

    @Operation(summary = "修改角色状态")
    @PutMapping("/status")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Void> updateStatus(@RequestParam Long roleId, @RequestParam Integer status) {
        roleService.updateStatus(roleId, status);
        return Result.success();
    }

    @Operation(summary = "分配角色权限")
    @PutMapping("/permissions/{roleId}")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Void> assignPermissions(
            @Parameter(description = "角色ID") @PathVariable Long roleId,
            @Parameter(description = "权限ID列表") @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(roleId, permissionIds);
        return Result.success();
    }

    @Operation(summary = "获取角色的权限ID列表")
    @GetMapping("/permissions/{roleId}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<List<Long>> getPermissionIds(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        return Result.success(roleService.getPermissionIds(roleId));
    }
} 