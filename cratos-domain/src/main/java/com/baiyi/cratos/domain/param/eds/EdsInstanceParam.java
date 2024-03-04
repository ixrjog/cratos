package com.baiyi.cratos.domain.param.eds;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:11
 * @Version 1.0
 */
public class EdsInstanceParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class InstancePageQuery extends PageParam {

        @Schema(description = "Query by name")
        private String queryName;

        @Schema(description = "Query by edsType")
        private String edsType;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RegisterInstance implements IToTarget<EdsInstance> {

        // private Integer id;

        @NotNull(message = "Parameter 'instanceName' cannot be empty.")
        private String instanceName;

        // private String instanceType;

        private String kind;

        private String version;

        private Boolean valid;

        @NotNull(message = "Parameter 'configId' cannot be empty.")
        private Integer configId;

        private String url;

        private String comment;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ImportInstanceAsset {

        @Schema(description = "Eds Instance ID")
        private Integer instanceId;

        // private String instanceType;

        private String assetType;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AssetPageQuery extends PageParam {

        private Integer instanceId;

        @Schema(description = "Query by name")
        private String queryName;

        @Schema(description = "Query by assetType")
        private String assetType;

        private Boolean valid;

    }

}
