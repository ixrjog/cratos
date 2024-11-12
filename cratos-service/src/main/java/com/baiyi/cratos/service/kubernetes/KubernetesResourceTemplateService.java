package com.baiyi.cratos.service.kubernetes;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.mapper.KubernetesResourceTemplateMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/1 10:55
 * &#064;Version 1.0
 */
public interface KubernetesResourceTemplateService extends BaseUniqueKeyService<KubernetesResourceTemplate, KubernetesResourceTemplateMapper>, BaseValidService<KubernetesResourceTemplate, KubernetesResourceTemplateMapper>, SupportBusinessService {

    DataTable<KubernetesResourceTemplate> queryTemplatePage(
            KubernetesResourceTemplateParam.TemplatePageQueryParam pageQuery);

}
