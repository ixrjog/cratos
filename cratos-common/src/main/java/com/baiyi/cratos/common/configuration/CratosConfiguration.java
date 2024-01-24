package com.baiyi.cratos.common.configuration;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/1/17 15:57
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "cratos", ignoreInvalidFields = true)
public class CratosConfiguration {

    private Auth auth;

    private Rbac rbac;

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
    public static class Rbac {
        private AutoConfiguration autoConfiguration;
    }

    @Data
    public static class AutoConfiguration {
        private Boolean enabled;
    }

    public boolean isTheResourceInTheWhiteList(String resourceName) {
        List<String> resources = Optional.of(auth)
                .map(Auth::getWhite)
                .map(White::getResources)
                .orElse(Lists.newArrayList());
        return resources.stream()
                .anyMatch(resource -> resourceName.indexOf(resource) == 0);
    }

}
