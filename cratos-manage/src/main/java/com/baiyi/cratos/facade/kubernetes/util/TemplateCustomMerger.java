package com.baiyi.cratos.facade.kubernetes.util;

import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.facade.kubernetes.provider.strategy.CustomStrategyFactory;
import org.springframework.util.CollectionUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/12 14:33
 * &#064;Version 1.0
 */
public class TemplateCustomMerger {

    private KubernetesResourceTemplateCustom.Custom from;
    private KubernetesResourceTemplateCustom.Custom to;
    private KubernetesResourceTemplateMember member;

    private KubernetesResourceTemplateCustom.Custom memberCustom;

    public static TemplateCustomMerger newBuilder() {
        return new TemplateCustomMerger();
    }

    public TemplateCustomMerger mergeFrom(KubernetesResourceTemplateCustom.Custom from) {
        this.from = from;
        return this;
    }

    public TemplateCustomMerger mergeTo(KubernetesResourceTemplateCustom.Custom to) {
        this.to = to;
        return this;
    }

    public TemplateCustomMerger member(KubernetesResourceTemplateMember member) {
        this.member = member;
        return this;
    }

    public TemplateCustomMerger merge() {
        KubernetesResourceTemplateCustom.Custom custom = KubernetesResourceTemplateCustom.Custom.builder()
                .build();
        custom.getData()
                .putAll(to.getData());
        custom.getData()
                .putAll(from.getData());
        if (!CollectionUtils.isEmpty(from.getInstances())) {
            custom.setInstances(from.getInstances());
        } else {
            custom.setInstances(to.getInstances());
        }
        custom.setStrategies(from.getStrategies());
        this.memberCustom = custom;
        return this;
    }

    private TemplateCustomMerger rewrite() {
        CustomStrategyFactory.rewrite(member, memberCustom);
        return this;
    }

    public KubernetesResourceTemplateCustom.Custom build() {
        this.rewrite();
        return memberCustom;
    }

}
