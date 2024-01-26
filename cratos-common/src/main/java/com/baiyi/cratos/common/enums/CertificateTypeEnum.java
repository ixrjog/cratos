package com.baiyi.cratos.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2024/1/3 14:00
 * @Version 1.0
 */
@Getter
public enum CertificateTypeEnum {

    // 亚马逊颁发
    AMAZON_ISSUED("Amazon Issued"),
    // 导入
    @Schema(description = "导入" )
    IMPORTED("Imported");

    private final String type;

    CertificateTypeEnum(String type) {
        this.type = type;
    }

}