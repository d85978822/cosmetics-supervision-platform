package com.cosmetics.system.controller;

import com.cosmetics.system.common.Result;
import com.cosmetics.system.model.entity.SysPermission;
import com.cosmetics.system.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "权限管理")
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class SysPermissionController {

    private final SysPermissionService permissionService;

    @Operation(summary = "获取权限树")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<List<SysPermission>> getPermissionTree() {
        return Result.success(permissionService.getPermissionTree());
    }

    @Operation(summary = "获取权限详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<SysPermission> getInfo(@Parameter(description = "权限ID") @PathVariable Long id) {
        return Result.success(permissionService.getById(id));
    }

    @Operation(summary = "新增权限")
    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Result<Void> add(@RequestBody SysPermission permission) {
        permissionService.save(permission);
        return Result.success();
    }

    @Operation(summary = "修改权限")
    @PutMapping
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Void> edit(@RequestBody SysPermission permission) {
        permissionService.updateById(permission);
        return Result.success();
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    public Result<Void> remove(@Parameter(description = "权限ID") @PathVariable Long id) {
        permissionService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "修改权限状态")
    @PutMapping("/status")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Void> updateStatus(@RequestParam Long permissionId, @RequestParam Integer status) {
        permissionService.updateStatus(permissionId, status);
        return Result.success();
    }

    @Operation(summary = "根据角色ID查询权限列表")
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<List<SysPermission>> getPermissionsByRoleId(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        return Result.success(permissionService.getPermissionsByRoleId(roleId));
    }

    @Operation(summary = "根据用户ID查询权限列表")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<List<SysPermission>> getPermissionsByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(permissionService.getPermissionsByUserId(userId));
    }
} 