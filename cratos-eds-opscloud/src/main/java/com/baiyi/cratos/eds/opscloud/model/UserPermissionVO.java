package com.baiyi.cratos.eds.opscloud.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/8/14 1:58 下午
 * @Version 1.0
 */
public class UserPermissionVO {

    @Data
    @NoArgsConstructor
    @Schema
    public static class UserPermission  implements Serializable {
        @Serial
        private static final long serialVersionUID = 4639108050065877992L;
        private Integer id;
        private Integer userId;
        private Integer businessId;
        private Integer businessType;
        private Integer rate;
        private String permissionRole;
        private String content;
    }

}