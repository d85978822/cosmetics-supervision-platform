package com.cosmetics.system.controller;

import com.cosmetics.system.common.Result;
import com.cosmetics.system.service.SysRolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色权限管理")
@RestController
@RequestMapping("/role-permission")
@RequiredArgsConstructor
public class SysRolePermissionController {

    private final SysRolePermissionService rolePermissionService;

    @Operation(summary = "分配角色权限")
    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Void> assignPermissions(
            @Parameter(description = "角色ID") @PathVariable Long roleId,
            @Parameter(description = "权限ID列表") @RequestBody List<Long> permissionIds) {
        rolePermissionService.assignPermissions(roleId, permissionIds);
        return Result.success();
    }

    @Operation(summary = "获取角色的权限ID列表")
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<List<Long>> getPermissionIds(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        return Result.success(rolePermissionService.getPermissionIds(roleId));
    }
} 