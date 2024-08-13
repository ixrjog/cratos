package com.baiyi.cratos.domain.param.server;

import com.baiyi.cratos.domain.generator.Server;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 上午10:52
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ServerParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ServerPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private Boolean valid;
        private String protocol;
    }

    @Data
    @Schema
    public static class AddServer implements IToTarget<Server> {
        @NotBlank
        private String name;
        private String displayName;
        private Integer serverGroupId;
        private String osType;
        @NotBlank
        private String envName;
        private String publicIp;
        @NotBlank
        private String privateIp;
        private String remoteMgmtIp;
        private String serverType;
        private String region;
        private String zone;
        private Integer monitorStatus;
        @NotNull
        private Boolean valid;
        private String serverStatus;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateServer implements IToTarget<Server> {
        @NotNull
        private Integer id;
        @NotBlank
        private String name;
        private String displayName;
        private Integer serverGroupId;
        private String osType;
        @NotBlank
        private String envName;
        private String publicIp;
        @NotBlank
        private String privateIp;
        private String remoteMgmtIp;
        private String serverType;
        private String region;
        private String zone;
        private Integer monitorStatus;
        @NotNull
        private Boolean valid;
        private String serverStatus;
        private String comment;
    }

}
