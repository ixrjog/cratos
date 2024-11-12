package com.baiyi.cratos.facade.kubernetes.util;

import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/12 17:02
 * &#064;Version 1.0
 */
public class KubernetesResourceTemplateBuilder {

    private KubernetesResourceTemplate template;
    public KubernetesResourceTemplateParam.CopyTemplate copyTemplate;

    public static KubernetesResourceTemplateBuilder newBuilder() {
        return new KubernetesResourceTemplateBuilder();
    }

    public KubernetesResourceTemplateBuilder template(KubernetesResourceTemplate template) {
        this.template = template;
        return this;
    }

    public KubernetesResourceTemplateBuilder copyTemplate(KubernetesResourceTemplateParam.CopyTemplate copyTemplate) {
        this.copyTemplate = copyTemplate;
        return this;
    }

    public KubernetesResourceTemplate copy() {
        return KubernetesResourceTemplate.builder()
                .templateKey(copyTemplate.getTemplateKey())
                .name(copyTemplate.getTemplateName())
                .custom(template.getCustom())
                .apiVersion(template.getApiVersion())
                .comment(template.getComment())
                .valid(true)
                .build();
    }

}
