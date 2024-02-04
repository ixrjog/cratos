package com.baiyi.cratos.common.configuration.model;

import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/4 16:57
 * @Version 1.0
 */
public class CratosModel {

    @Data
    public static class Auth {
        private White white;
        private Boolean enabled;
    }

    @Data
    public static class White {
        private List<String> resources;
    }

    @Data
    public static class Credential {
        private Boolean highSecurity;
    }

    @Data
    public static class Rbac {
        private AutoConfiguration autoConfiguration;
    }

    @Data
    public static class AutoConfiguration {
        private Boolean enabled;
    }

}
