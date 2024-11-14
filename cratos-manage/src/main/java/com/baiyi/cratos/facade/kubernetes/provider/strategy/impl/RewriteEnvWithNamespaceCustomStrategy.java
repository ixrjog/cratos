package com.baiyi.cratos.facade.kubernetes.provider.strategy.impl;

import com.baiyi.cratos.common.enums.StrategyEnum;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.facade.kubernetes.provider.strategy.CustomStrategy;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/8 14:17
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class RewriteEnvWithNamespaceCustomStrategy implements CustomStrategy {

    private final static String DEF_ENV_KEY = "envName";

    @Override
    public String getName() {
        return StrategyEnum.REWRITE_ENV_WITH_NAMESPACE.name();
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public void rewrite(KubernetesResourceTemplateCustom.Strategy strategy, KubernetesResourceTemplateMember member,
                        KubernetesResourceTemplateCustom.Custom custom) {
        final String envKey = Optional.of(strategy)
                .map(KubernetesResourceTemplateCustom.Strategy::getValue)
                .orElse(DEF_ENV_KEY);
        final String envName = member.getNamespace();
        Map<String, String> data = Maps.newHashMap();
        // 替换环境变量
        custom.getData()
                .forEach((k, v) -> {
                    final String placeholder = "${" + envKey + "}";
                    if (StringUtils.hasText(v)) {
                        if (v.contains(placeholder)) {
                            data.put(k, v.replace(placeholder, envName));
                        } else {
                            data.put(k, v);
                        }
                    }
                });
        // 设置env环境变量
        data.put(envKey, envName);
        custom.setData(data);
    }

}
