package com.baiyi.cratos.domain.view.robot;

import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.SensitiveType;
import com.baiyi.cratos.domain.view.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 17:17
 * &#064;Version 1.0
 */
public class RobotVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Robot extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -6701169021007151611L;
        private Integer id;
        private String name;
        private String username;
        @FieldSensitive(type = SensitiveType.PASSWORD)
        private String token;
        private Boolean valid;
        private Boolean trail;
        private String createdBy;
        @Schema(description = "Expired time")
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expiredTime;
        private String comment;
    }

    @Data
    @Schema
    public static class RobotToken implements Serializable {
        @Serial
        private static final long serialVersionUID = -2585293703264128818L;
        private String name;
        private String username;
        private String token;
        @Schema(description = "Expired time")
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expiredTime;
    }

}
