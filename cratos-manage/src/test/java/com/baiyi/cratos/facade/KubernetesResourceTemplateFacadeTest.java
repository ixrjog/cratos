package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/4 15:28
 * &#064;Version 1.0
 */
public class KubernetesResourceTemplateFacadeTest extends BaseUnit {

    @Resource
    private KubernetesResourceTemplateFacade kubernetesResourceTemplateFacade;

    @Test
    void test() {
        KubernetesResourceTemplateParam.TemplatePageQuery pageQuery = KubernetesResourceTemplateParam.TemplatePageQuery.builder()
                .page(1)
                .length(10)
                .build();
        DataTable<KubernetesResourceTemplateVO.Template> table = kubernetesResourceTemplateFacade.queryTemplatePage(
                pageQuery);
        System.out.println(table);
    }

}
