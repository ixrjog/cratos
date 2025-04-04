package com.baiyi.cratos.domain.view.user;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.enums.SensitiveType;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasResourceCount;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/10 09:58
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class UserVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.USER)
    public static class User extends BaseVO implements BaseBusiness.IBusinessAnnotate,
            // Business
            BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, RbacRoleVO.IRbacRoles, HasResourceCount, Serializable {

        @Serial
        private static final long serialVersionUID = 7642003154901654181L;
        private Integer id;
        private String username;
        private String uuid;
        private String name;
        private String displayName;
        @FieldSensitive(type = SensitiveType.EMAIL)
        private String email;
        private Boolean valid;
        @Schema(description = "最后登录时间")
        private Date lastLogin;
        @FieldSensitive(type = SensitiveType.MOBILE_PHONE)
        private String mobilePhone;
        private Integer otp;
        private String createdBy;
        private String source;
        private String lang;
        @FieldSensitive(type = SensitiveType.PASSWORD)
        private String password;
        private String comment;
        @Schema(description = "Expired time")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;
        private Boolean locked;

        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;
        @Schema(description = "Rbac Roles")
        private List<RbacRoleVO.Role> rbacRoles;
        @Schema(description = "Resource Count")
        private Map<String, Integer> resourceCount;

        private UserAvatar avatar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UserAvatar implements Serializable {
        @Serial
        private static final long serialVersionUID = 8580940372670729638L;
        public static final UserAvatar NO_DATA = UserAvatar.builder()
                .valid(false)
                .build();
        @Builder.Default
        private Boolean valid = true;
        private String source;
        private String url;
    }

}
