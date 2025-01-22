package com.baiyi.cratos.domain.view.user;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Permission extends BaseVO implements EnvVO.HasEnv, Serializable {
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
        private EnvVO.Env env;
        @Override
        public String getEnvName() {
            return this.role;
        }
    }

    @Data
    @Builder
    @Schema
    public static class BusinessUserPermissionDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 8569271131451422138L;
        public static final BusinessUserPermissionDetails EMPTY = BusinessUserPermissionDetails.builder()
                .build();
        @Builder.Default
        private Map<String, List<UserPermissionBusiness>> businessPermissions = Map.of();
    }

    @Data
    @Builder
    @Schema
    public static class UserPermissionDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 8108421939569005708L;
        public static final UserPermissionDetails EMPTY = UserPermissionDetails.builder()
                .build();
        @Builder.Default
        private List<UserPermissionBusiness> userPermissions = List.of();
    }

    @Data
    @Builder
    @Schema
    public static class UserPermissionBusiness implements BaseBusiness.HasBusiness, Serializable {
        @Serial
        private static final long serialVersionUID = -3544879369613177893L;
        private String businessType;
        private Integer businessId;
        private String name;
        private String displayName;
        private List<Permission> userPermissions;
    }

}
