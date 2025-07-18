package com.baiyi.cratos.facade;


import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.HasSetValid;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/1 14:05
 * &#064;Version 1.0
 */
public interface KubernetesResourceTemplateFacade extends HasSetValid {

    DataTable<KubernetesResourceTemplateVO.Template> queryTemplatePage(
            KubernetesResourceTemplateParam.TemplatePageQuery pageQuery);

    void lockTemplate(KubernetesResourceTemplateParam.LockTemplate lockTemplate);

    KubernetesResourceTemplateVO.Template getTemplateById(int id);

    void addTemplate(KubernetesResourceTemplateParam.AddTemplate addTemplate);

    void updateTemplate(KubernetesResourceTemplateParam.UpdateTemplate updateTemplate);

    void deleteById(int id);

    KubernetesResourceTemplateVO.Template copyTemplate(KubernetesResourceTemplateParam.CopyTemplate copyTemplate);

    void createResourceByTemplate(KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate);

}
