package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午10:39
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsDingtalkConfigModel {

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
