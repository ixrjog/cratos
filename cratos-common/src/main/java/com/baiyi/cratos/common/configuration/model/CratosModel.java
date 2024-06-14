package com.baiyi.cratos.common.configuration.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/4 16:57
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
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
