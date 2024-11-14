package com.baiyi.cratos.facade.kubernetes.provider.strategy.impl;

import com.baiyi.cratos.common.enums.StrategyEnum;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.facade.kubernetes.provider.strategy.CustomStrategy;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/8 11:45
 * &#064;Version 1.0
 */
@Component
public class WipeTheHostEnvSuffixCustomStrategy implements CustomStrategy {

    private final static String DEF_HOST_KEY = "host";

    @Override
    public String getName() {
        return StrategyEnum.WIPE_THE_HOST_ENV_SUFFIX.name();
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void rewrite(KubernetesResourceTemplateCustom.Strategy strategy, KubernetesResourceTemplateMember member,
                        KubernetesResourceTemplateCustom.Custom custom) {
        final String hostKey = Optional.of(strategy)
                .map(KubernetesResourceTemplateCustom.Strategy::getValue)
                .orElse(DEF_HOST_KEY);
        if (custom.getData()
                .containsKey(hostKey)) {
            final String hostValue = custom.getData()
                    .get(hostKey);
            // -prod.
            final String envSuffix = "-" + member.getNamespace() + ".";
            if (hostValue.endsWith(envSuffix) && member.getNamespace()
                    .equals("prod")) {
                custom.getData()
                        .put(hostKey, hostValue.replace(envSuffix, "."));
            }
        }
    }

}
