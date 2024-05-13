package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/3/1 11:11
 * @Version 1.0
 */
public class EdsAwsConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Aws implements IEdsConfigModel {

        private Cred cred;

        private String regionId;

        private Set<String> regionIds;

        private Ec2 ec2;

        private EdsInstance edsInstance;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {

        private String id;

        private String name;

        @Schema(description = "可选项公司")
        private String company;

        private String accessKey;

        private String secretKey;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Ec2 {

        private String instances;

    }

}
