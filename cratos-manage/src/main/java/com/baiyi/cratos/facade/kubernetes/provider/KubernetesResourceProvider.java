package com.baiyi.cratos.facade.kubernetes.provider;

import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.facade.kubernetes.provider.factory.KubernetesResourceProviderFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/6 13:53
 * &#064;Version 1.0
 */
public interface KubernetesResourceProvider<A> extends InitializingBean {

    String getKind();

    EdsAsset produce(KubernetesResourceTemplateMember member, KubernetesResourceTemplateCustom.Custom custom);

    default void afterPropertiesSet() {
        KubernetesResourceProviderFactory.register(this);
    }

    default KubernetesResourceTemplateCustom.KubernetesInstance findOf(String namespace,
                                                                       KubernetesResourceTemplateCustom.Custom custom) {
        if (StringUtils.hasText(namespace)) {
            throw new KubernetesResourceTemplateException("The namespace is empty.");
        }
        return custom.getInstances()
                .stream()
                .filter(instance -> instance.getNamespaces()
                        .stream()
                        .anyMatch(e -> namespace.equals(e)))
                .findFirst()
                .orElse(null);
    }

}
