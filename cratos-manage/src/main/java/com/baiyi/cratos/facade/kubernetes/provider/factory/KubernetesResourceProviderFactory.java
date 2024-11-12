package com.baiyi.cratos.facade.kubernetes.provider.factory;

import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.facade.kubernetes.provider.KubernetesResourceProvider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/6 13:51
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class KubernetesResourceProviderFactory {

    private final static ConcurrentHashMap<String, KubernetesResourceProvider<?>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(KubernetesResourceProvider<?> bean) {
        log.debug("KubernetesResourceProviderFactory Registered: {}", bean.getClass()
                .getSimpleName());
        CONTEXT.put(bean.getKind(), bean);
    }

    public static <T> KubernetesResourceProvider<?> getProvider(String kind) {
        if (!CONTEXT.containsKey(kind)) {
            throw new KubernetesResourceTemplateException("Kind is invalid.");
        }
        return CONTEXT.get(kind);
    }

    public static List<EdsAsset> produce(KubernetesResourceTemplateMember member,
                                         KubernetesResourceTemplateCustom.Custom custom) {
        if (!CONTEXT.containsKey(member.getKind())) {
            throw new KubernetesResourceTemplateException("Kind is invalid.");
        }
        return CONTEXT.get(member.getKind())
                .produce(member, custom);
    }

}
