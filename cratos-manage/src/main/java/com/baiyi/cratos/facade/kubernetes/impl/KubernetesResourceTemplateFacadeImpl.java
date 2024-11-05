package com.baiyi.cratos.facade.kubernetes.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateFacade;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateMemberService;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateService;
import com.baiyi.cratos.wrapper.kubernetes.KubernetesResourceTemplateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/1 14:05
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesResourceTemplateFacadeImpl implements KubernetesResourceTemplateFacade {

    private final KubernetesResourceTemplateService kubernetesResourceTemplateService;
    private final KubernetesResourceTemplateWrapper kubernetesResourceTemplateWrapper;
    private final KubernetesResourceTemplateMemberService kubernetesResourceTemplateMemberService;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE)
    public DataTable<KubernetesResourceTemplateVO.Template> queryTemplatePage(
            KubernetesResourceTemplateParam.TemplatePageQuery pageQuery) {
        DataTable<KubernetesResourceTemplate> table = kubernetesResourceTemplateService.queryTemplatePage(
                pageQuery.toParam());
        return kubernetesResourceTemplateWrapper.wrapToTarget(table);
    }

    @Override
    public KubernetesResourceTemplateVO.Template getTemplateById(int id) {
        KubernetesResourceTemplate template = kubernetesResourceTemplateService.getById(id);
        if (template == null) {
            throw new KubernetesResourceTemplateException("Template does not exist.");
        }
        return kubernetesResourceTemplateWrapper.wrapToTarget(template);
    }

    @Override
    public void addTemplate(KubernetesResourceTemplateParam.AddTemplate addTemplate) {
        KubernetesResourceTemplate kubernetesResourceTemplate = addTemplate.toTarget();
        kubernetesResourceTemplateService.add(kubernetesResourceTemplate);
    }

    @Override
    public void updateTemplate(KubernetesResourceTemplateParam.UpdateTemplate updateTemplate) {
        KubernetesResourceTemplate kubernetesResourceTemplate = kubernetesResourceTemplateService.getById(
                updateTemplate.getId());
        if (kubernetesResourceTemplate == null) {
            return;
        }
        kubernetesResourceTemplate.setName(updateTemplate.getName());
        kubernetesResourceTemplate.setApiVersion(updateTemplate.getApiVersion());
        kubernetesResourceTemplateService.updateByPrimaryKey(kubernetesResourceTemplate);
    }

    @Override
    public void setValidById(int id) {
        kubernetesResourceTemplateService.updateValidById(id);
    }

    @Override
    public void deleteById(int id) {
        // TODO
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KubernetesResourceTemplateVO.Template copyTemplate(
            KubernetesResourceTemplateParam.CopyTemplate copyTemplate) {
        KubernetesResourceTemplate kubernetesResourceTemplate = kubernetesResourceTemplateService.getById(
                copyTemplate.getTemplateId());
        if (kubernetesResourceTemplate == null) {
            throw new KubernetesResourceTemplateException("Template does not exist.");
        }
        // copy template
        KubernetesResourceTemplate newTemplate = KubernetesResourceTemplate.builder()
                .templateKey(copyTemplate.getTemplateKey())
                .name(copyTemplate.getTemplateName())
                .custom(kubernetesResourceTemplate.getCustom())
                .apiVersion(kubernetesResourceTemplate.getApiVersion())
                .comment(kubernetesResourceTemplate.getComment())
                .valid(true)
                .build();
        kubernetesResourceTemplateService.add(newTemplate);
        // copy members
        List<KubernetesResourceTemplateMember> members = kubernetesResourceTemplateMemberService.queryMemberByTemplateId(
                copyTemplate.getTemplateId(), true);
        if (!CollectionUtils.isEmpty(members)) {
            for (KubernetesResourceTemplateMember member : members) {
                member.setTemplateId(newTemplate.getId());
                kubernetesResourceTemplateMemberService.add(member);
            }
        }
        return this.getTemplateById(copyTemplate.getTemplateId());
    }

}
