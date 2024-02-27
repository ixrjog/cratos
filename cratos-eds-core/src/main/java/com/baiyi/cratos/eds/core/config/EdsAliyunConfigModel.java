package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/2/26 10:52
 * @Version 1.0
 */
@Data
public class EdsAliyunConfigModel {

    private Aliyun aliyun;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Aliyun implements IEdsConfigModel {

        private String version;
        // default
        private String regionId;

        private Set<String> regionIds;

        @Schema(description = "凭据")
        private Cred cred;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {

        private String uid;
        private String name;
        private String company;
        private String accessKeyId;
        private String accessKeySecret;

    }

}
