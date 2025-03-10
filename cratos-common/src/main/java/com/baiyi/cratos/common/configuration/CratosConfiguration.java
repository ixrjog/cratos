package com.baiyi.cratos.common.configuration;

import com.baiyi.cratos.common.configuration.model.CratosModel;
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

    private CratosModel.Auth auth;
    private CratosModel.Rbac rbac;
    private CratosModel.Credential credential;

    public boolean isWhitelistResource(String resourceName) {
        List<String> resources = Optional.of(auth)
                .map(CratosModel.Auth::getWhite)
                .map(CratosModel.White::getResources)
                .orElse(Lists.newArrayList());
        return resources.stream()
                .anyMatch(resource -> resourceName.indexOf(resource) == 0);
    }

}
