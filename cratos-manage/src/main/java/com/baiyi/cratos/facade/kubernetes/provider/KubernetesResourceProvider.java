package com.baiyi.cratos.facade.kubernetes.provider;

import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.facade.kubernetes.provider.factory.KubernetesResourceProviderFactory;
import org.springframework.beans.factory.InitializingBean;

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

}
