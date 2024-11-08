package com.baiyi.cratos.facade.kubernetes.provider.strategy.impl;

import com.baiyi.cratos.common.enums.StrategyEnum;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.common.util.BeetlUtil;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.facade.kubernetes.provider.strategy.CustomStrategy;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
                .orElse("envName");
        final String envName = member.getNamespace();
        Map<String, String> data = Maps.newHashMap();
        custom.getData()
                .forEach((k, v) -> {
                    try {
                        Map<String, String> contentMap = Maps.newHashMap();
                        contentMap.put(envKey, member.getNamespace());
                        String newValue = BeetlUtil.renderTemplate2(v, contentMap);
                        data.put(k, newValue);
                    } catch (IOException ioException) {
                        log.debug(ioException.getMessage());
                        data.put(k, v);
                    }
                });
        custom.setData(data);
    }

}
