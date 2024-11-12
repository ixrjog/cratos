package com.baiyi.cratos.facade.kubernetes.util;

import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/12 15:04
 * &#064;Version 1.0
 */
public class KubernetesResourceBuilder {

    private KubernetesResourceTemplateMember member;
    private EdsAsset edsAsset;
    private String createdBy;
    private KubernetesResourceTemplateCustom.Custom memberCustom;

    public static KubernetesResourceBuilder newBuilder() {
        return new KubernetesResourceBuilder();
    }

    public KubernetesResourceBuilder member(KubernetesResourceTemplateMember member) {
        this.member = member;
        return this;
    }

    public KubernetesResourceBuilder edsAsset(EdsAsset edsAsset) {
        this.edsAsset = edsAsset;
        return this;
    }

    public KubernetesResourceBuilder createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public KubernetesResourceBuilder memberCustom(KubernetesResourceTemplateCustom.Custom memberCustom) {
        this.memberCustom = memberCustom;
        return this;
    }

    public KubernetesResource build() {
        return KubernetesResource.builder()
                .templateId(member.getTemplateId())
                .memberId(member.getId())
                .assetId(edsAsset.getId())
                .kind(member.getKind())
                .name(edsAsset.getName())
                .namespace(member.getNamespace())
                .custom(memberCustom.dump())
                .edsInstanceId(edsAsset.getInstanceId())
                .createdBy(createdBy)
                .build();
    }

}
