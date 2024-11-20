package com.baiyi.cratos.domain.param.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 10:00
 * &#064;Version 1.0
 */
public class UserPermissionParam {

    @Data
    @Schema
    @Builder
    public static class GrantUserPermission {
        private String username;
        private String name;
        private String displayName;
        private String businessType;
        private Integer businessId;
        private String role;
        private Boolean valid;
        private Integer seq;
        private String content;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;
        private String comment;
    }

}
