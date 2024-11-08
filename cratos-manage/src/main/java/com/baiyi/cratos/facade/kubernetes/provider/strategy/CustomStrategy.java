package com.baiyi.cratos.facade.kubernetes.provider.strategy;

import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/8 11:38
 * &#064;Version 1.0
 */
public interface CustomStrategy extends InitializingBean {

    String getName();

    int getOrder();

    void rewrite(KubernetesResourceTemplateCustom.Strategy strategy, KubernetesResourceTemplateMember member,
                 KubernetesResourceTemplateCustom.Custom custom);

    default void afterPropertiesSet() {
        CustomStrategyFactory.register(this);
    }

}
