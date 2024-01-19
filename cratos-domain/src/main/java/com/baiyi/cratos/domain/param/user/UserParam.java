package com.baiyi.cratos.domain.param.user;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:18
 * @Version 1.0
 */
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
    public static class AddUser implements IToTarget<User> {

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
