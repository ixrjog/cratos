package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午10:39
 * @Version 1.0
 */
public class EdsDingtalkConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Dingtalk implements IEdsConfigModel {

        private String version;
        private String url;
        private String company;
        private String corpId;
        private DingtalkApp app;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Robot implements IEdsConfigModel {

        private String token;
        private String desc;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class DingtalkApp {
        private String name;
        private String agentId;
        private String miniAppId;
        private String appKey;
        private String appSecret;
        private Department department;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Department {
        private Set<Long> deptIds;
    }


}
