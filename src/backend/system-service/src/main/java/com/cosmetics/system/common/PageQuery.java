package com.cosmetics.system.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分页查询参数")
public class PageQuery {
    
    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;
    
    @Schema(description = "每页数量", example = "10")
    private Integer pageSize = 10;
    
    @Schema(description = "排序字段")
    private String orderBy;
    
    @Schema(description = "排序方式：asc-升序，desc-降序")
    private String orderType;
} 