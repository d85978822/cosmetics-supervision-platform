package com.cosmetics.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "验证码响应结果")
public class CaptchaResponse {
    
    @Schema(description = "验证码key")
    private String key;
    
    @Schema(description = "验证码图片（Base64编码）")
    private String image;
} 