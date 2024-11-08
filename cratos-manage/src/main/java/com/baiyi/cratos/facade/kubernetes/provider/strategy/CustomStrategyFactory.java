package com.baiyi.cratos.facade.kubernetes.provider.strategy;

import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/8 11:34
 * &#064;Version 1.0
 */
@Slf4j
public class CustomStrategyFactory {

    private final static ConcurrentHashMap<String, CustomStrategy> CONTEXT = new ConcurrentHashMap<>();

    public static void register(CustomStrategy bean) {
        log.debug("CustomStrategyFactory Registered: {}", bean.getClass()
                .getSimpleName());
        CONTEXT.put(bean.getName(), bean);
    }

    public static CustomStrategy getStrategy(String name) {
        if (!CONTEXT.containsKey(name)) {
            return null;
        }
        return CONTEXT.get(name);
    }

    public static void rewrite(KubernetesResourceTemplateMember member,
                               KubernetesResourceTemplateCustom.Custom custom) {
        if (CollectionUtils.isEmpty(custom.getStrategies())) {
            return;
        }
        custom.getStrategies()
                .stream()
                .peek(e -> {
                    if (e.getOrder() == null) {
                        CustomStrategy customStrategy = getStrategy(e.getName());
                        if (customStrategy != null) {
                            e.setOrder(customStrategy.getOrder());
                        } else {
                            e.setOrder(0);
                        }
                    }
                })
                .sorted(Comparator.comparingInt(KubernetesResourceTemplateCustom.Strategy::getOrder))
                .forEach(e -> {
                    CustomStrategy customStrategy = getStrategy(e.getName());
                    if (customStrategy != null) {
                        customStrategy.rewrite(e, member, custom);
                    }
                });
    }

}
