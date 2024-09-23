package com.baiyi.cratos.domain.param.robot;

import com.baiyi.cratos.domain.ISetSessionUser;
import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 17:20
 * &#064;Version 1.0
 */
public class RobotParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RobotPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
    }

    @Data
    @Schema
    public static class AddRobot implements IToTarget<Robot>, ISetSessionUser {
        @Null
        private Integer id;
        private String name;
        @NotBlank
        private String username;
        @Null
        private String token;
        private Boolean valid;
        private Boolean trail;
        private String createdBy;
        private Date expiredTime;
        private String comment;

        @Override
        public void setSessionUser(String username) {
            this.createdBy = username;
        }
    }

    @Data
    @Schema
    public static class ApplyRobot implements IToTarget<Robot>, ISetSessionUser {
        @Null
        private Integer id;
        private String name;
        @Null
        private String username;
        @Null
        private String token;
        private Boolean valid;
        private Boolean trail;
        private String createdBy;
        private Date expiredTime;
        private String comment;

        @Override
        public void setSessionUser(String username) {
            this.username = username;
            this.createdBy = username;
        }
    }

    @Data
    @Schema
    public static class RevokeRobot implements ISetSessionUser {
        @NotNull
        private Integer id;
        @Null
        private String operatingBy;

        @Override
        public void setSessionUser(String username) {
            this.operatingBy = username;
        }
    }

}
