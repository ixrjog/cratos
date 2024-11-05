package com.baiyi.cratos.facade.kubernetes;


import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/1 14:05
 * &#064;Version 1.0
 */
public interface KubernetesResourceTemplateFacade {

    DataTable<KubernetesResourceTemplateVO.Template> queryTemplatePage(
            KubernetesResourceTemplateParam.TemplatePageQuery pageQuery);

    KubernetesResourceTemplateVO.Template getTemplateById(int id);

    void addTemplate(KubernetesResourceTemplateParam.AddTemplate addTemplate);

    void updateTemplate(KubernetesResourceTemplateParam.UpdateTemplate updateTemplate);

    void setValidById(int id);

    void deleteById(int id);

    KubernetesResourceTemplateVO.Template copyTemplate(KubernetesResourceTemplateParam.CopyTemplate copyTemplate);

}
