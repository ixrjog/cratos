package com.baiyi.cratos.facade.kubernetes.util;

import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/12 17:09
 * &#064;Version 1.0
 */
public class KubernetesResourceTemplateMemberBuilder {

    private KubernetesResourceTemplateMember member;

    private KubernetesResourceTemplate template;

    public static KubernetesResourceTemplateMemberBuilder newBuilder() {
        return new KubernetesResourceTemplateMemberBuilder();
    }

    public KubernetesResourceTemplateMemberBuilder member(KubernetesResourceTemplateMember member) {
        this.member = member;
        return this;
    }

    public KubernetesResourceTemplateMemberBuilder template(KubernetesResourceTemplate template) {
        this.template = template;
        return this;
    }

    public KubernetesResourceTemplateMember copy() {
        this.member.setId(null);
        this.member.setTemplateId(template.getId());
        return member;
    }

}
