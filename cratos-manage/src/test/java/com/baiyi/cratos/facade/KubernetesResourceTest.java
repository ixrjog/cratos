package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceTemplateParam;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/11 10:13
 * &#064;Version 1.0
 */
public class KubernetesResourceTest extends BaseUnit {

    @Resource
    private KubernetesResourceTemplateFacade kubernetesResourceTemplateFacade;

    @Test
    void test() {
        KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate = KubernetesResourceTemplateParam.CreateResourceByTemplate.builder()
                .templateId(1)
                .build();
        //   kubernetesResourceTemplateFacade.createResourceByTemplate(createResourceByTemplate);
    }

}
