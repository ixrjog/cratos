package com.baiyi.cratos.domain.view.user;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.view.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/6 15:43
 * &#064;Version 1.0
 */
public class UserPermissionVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Permission extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -4844155821760523819L;
        private Integer id;
        private String username;
        private String name;
        private String displayName;
        private String businessType;
        private Integer businessId;
        private String role;
        private Boolean valid;
        private Integer seq;
        private String content;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expiredTime;
        private String comment;
    }

    @Data
    @Builder
    @Schema
    public static class PermissionDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = -8019733940094649908L;
        // Map<{businessType}, Map<{businessId}, MergedPermissions>>
        private Map<String, Map<Integer, MergedPermissions>> permissions;
    }

    @Data
    @Builder
    @Schema
    public static class MergedPermissions implements Serializable {
        @Serial
        private static final long serialVersionUID = 8090775323820190804L;
        private String businessType;
        private Integer businessId;
        @Builder.Default
        private List<PermissionRole> roles = Lists.newArrayList();
    }

    @Data
    @Builder
    @Schema
    public static class PermissionRole implements Serializable {
        @Serial
        private static final long serialVersionUID = -3454331207462892087L;
        private String role;
        private Boolean valid;
        private Integer seq;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expiredTime;
        private String comment;
    }

}
