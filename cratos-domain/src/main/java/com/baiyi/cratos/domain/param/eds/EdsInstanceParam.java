package com.baiyi.cratos.domain.param.eds;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

        private Integer id;

        private String instanceName;

        private String instanceType;

        private String kind;

        private String version;

        private Boolean valid;

        private Integer configId;

        private String url;

        private String comment;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ImportInstanceAsset {

        @Schema(description = "Eds Instance ID")
        private Integer instanceId;

       // private String instanceType;

        private String assetType;

    }

}
