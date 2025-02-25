package com.cosmetics.system.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_permission")
public class SysPermission {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long parentId;
    
    private String permissionName;
    
    private String permissionKey;
    
    private String permissionType;
    
    private String path;
    
    private String component;
    
    private String redirect;
    
    private String icon;
    
    private Integer sort;
    
    private Integer status;
    
    private Boolean visible;
    
    private Boolean keepAlive;
    
    private String remark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private List<SysPermission> children;
} 