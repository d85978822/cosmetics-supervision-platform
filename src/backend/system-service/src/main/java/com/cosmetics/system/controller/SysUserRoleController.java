package com.cosmetics.system.controller;

import com.cosmetics.system.common.Result;
import com.cosmetics.system.service.SysUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户角色管理")
@RestController
@RequestMapping("/user-role")
@RequiredArgsConstructor
public class SysUserRoleController {

    private final SysUserRoleService userRoleService;

    @Operation(summary = "分配用户角色")
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> assignRoles(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        userRoleService.assignRoles(userId, roleIds);
        return Result.success();
    }

    @Operation(summary = "获取用户的角色ID列表")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<List<Long>> getRoleIds(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(userRoleService.getRoleIds(userId));
    }
} 