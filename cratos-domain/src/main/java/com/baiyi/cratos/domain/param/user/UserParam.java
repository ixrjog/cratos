package com.baiyi.cratos.domain.param.user;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.IImportFromAsset;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:18
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class UserParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UserPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
    }

    @Data
    @Schema
    @Builder
    @BusinessType(type = BusinessTypeEnum.USER)
    public static class AddUser implements IToTarget<User>, IImportFromAsset {
        private Integer id;
        private String username;
        private String uuid;
        private String name;
        private String displayName;
        private String email;
        private Boolean valid;
        private Boolean locked;
        private Date lastLogin;
        private String mobilePhone;
        private Integer otp;
        private String createdBy;
        private String source;
        private String lang;
        private String password;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;
        private String comment;
        @Schema(description = "Import from assetId")
        private Integer fromAssetId;
    }

    @Data
    @Schema
    @Builder
    @BusinessType(type = BusinessTypeEnum.USER)
    public static class UpdateUser implements IToTarget<User> {
        private Integer id;
        private String username;
        private String uuid;
        private String name;
        private String displayName;
        private String email;
        private Boolean valid;
        private Boolean locked;
        private Date lastLogin;
        private String mobilePhone;
        private Integer otp;
        private String createdBy;
        private String source;
        private String lang;
        private String password;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;
        private String comment;
    }

    @Data
    @Schema
    @Builder
    @BusinessType(type = BusinessTypeEnum.USER)
    public static class UpdateMy implements IToTarget<User> {
        private Integer id;
        private String username;
        private String uuid;
        private String name;
        private String displayName;
        private String email;
        private Boolean valid;
        private Date lastLogin;
        private String mobilePhone;
        private Integer otp;
        private String createdBy;
        private String source;
        private String lang;
        private String password;
        private String comment;
    }

    @Data
    @Schema
    @Builder
    public static class ResetPassword {
        private String password;
    }

    @Data
    @Schema
    @Builder
    public static class UpdatePassword {
        private String username;
        private String password;
    }

}
