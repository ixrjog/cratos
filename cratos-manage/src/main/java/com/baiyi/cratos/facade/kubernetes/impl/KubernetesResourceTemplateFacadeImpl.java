package com.baiyi.cratos.facade.kubernetes.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateFacade;
import com.baiyi.cratos.facade.kubernetes.provider.KubernetesResourceProvider;
import com.baiyi.cratos.facade.kubernetes.provider.factory.KubernetesResourceProviderFactory;
import com.baiyi.cratos.facade.kubernetes.provider.strategy.CustomStrategyFactory;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceService;
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

    private final KubernetesResourceTemplateService templateService;
    private final KubernetesResourceTemplateWrapper templateWrapper;
    private final KubernetesResourceTemplateMemberService templateMemberService;
    private final KubernetesResourceService resourceService;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE)
    public DataTable<KubernetesResourceTemplateVO.Template> queryTemplatePage(
            KubernetesResourceTemplateParam.TemplatePageQuery pageQuery) {
        DataTable<KubernetesResourceTemplate> table = templateService.queryTemplatePage(pageQuery.toParam());
        return templateWrapper.wrapToTarget(table);
    }

    @Override
    public KubernetesResourceTemplateVO.Template getTemplateById(int id) {
        KubernetesResourceTemplate template = templateService.getById(id);
        if (template == null) {
            throw new KubernetesResourceTemplateException("Template does not exist.");
        }
        return templateWrapper.wrapToTarget(template);
    }

    @Override
    public void addTemplate(KubernetesResourceTemplateParam.AddTemplate addTemplate) {
        KubernetesResourceTemplate kubernetesResourceTemplate = addTemplate.toTarget();
        templateService.add(kubernetesResourceTemplate);
    }

    @Override
    public void updateTemplate(KubernetesResourceTemplateParam.UpdateTemplate updateTemplate) {
        KubernetesResourceTemplate kubernetesResourceTemplate = templateService.getById(updateTemplate.getId());
        if (kubernetesResourceTemplate == null) {
            return;
        }
        kubernetesResourceTemplate.setName(updateTemplate.getName());
        kubernetesResourceTemplate.setApiVersion(updateTemplate.getApiVersion());
        kubernetesResourceTemplate.setCustom(updateTemplate.getCustom());
        templateService.updateByPrimaryKey(kubernetesResourceTemplate);
    }

    @Override
    public void setValidById(int id) {
        templateService.updateValidById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        KubernetesResourceTemplate template = templateService.getById(id);
        if (template == null) {
            return;
        }
        templateMemberService.queryMemberByTemplateId(id)
                .forEach(member -> {
                    templateMemberService.deleteById(member.getId());
                });

        templateService.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KubernetesResourceTemplateVO.Template copyTemplate(
            KubernetesResourceTemplateParam.CopyTemplate copyTemplate) {
        KubernetesResourceTemplate kubernetesResourceTemplate = templateService.getById(copyTemplate.getTemplateId());
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
        templateService.add(newTemplate);
        // copy members
        List<KubernetesResourceTemplateMember> members = templateMemberService.queryMemberByTemplateId(
                copyTemplate.getTemplateId(), true);
        if (!CollectionUtils.isEmpty(members)) {
            for (KubernetesResourceTemplateMember member : members) {
                member.setTemplateId(newTemplate.getId());
                templateMemberService.add(member);
            }
        }
        return this.getTemplateById(copyTemplate.getTemplateId());
    }

    @Override
    @SetSessionUserToParam(desc = "set CreatedBy")
    public void createResourceByTemplate(
            KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate) {
        KubernetesResourceTemplateCustom.Custom templateCustom = getCustom(createResourceByTemplate);
        List<KubernetesResourceTemplateMember> members = templateMemberService.queryMemberByTemplateId(
                createResourceByTemplate.getTemplateId(), true);
        if (!CollectionUtils.isEmpty(members)) {
            for (KubernetesResourceTemplateMember member : members) {
                createResourceByTemplate(member, templateCustom, createResourceByTemplate.getCreatedBy());
            }
        }
    }

    private void createResourceByTemplate(KubernetesResourceTemplateMember member,
                                          KubernetesResourceTemplateCustom.Custom templateCustom, String createdBy) {
        KubernetesResourceTemplateCustom.Custom mainCustom = KubernetesResourceTemplateCustom.loadAs(
                member.getCustom());
        KubernetesResourceTemplateCustom.Custom memberCustom = KubernetesResourceTemplateCustom.merge(templateCustom,
                mainCustom);
        // 自定义策略工程重写
        CustomStrategyFactory.rewrite(member, memberCustom);
        KubernetesResourceProvider<?> provider = KubernetesResourceProviderFactory.getProvider(member.getKind());
        EdsAsset edsAsset = provider.produce(member, memberCustom);
        // 资产关联
        KubernetesResource resource = KubernetesResource.builder()
                .templateId(member.getTemplateId())
                .memberId(member.getId())
                .assetId(edsAsset.getId())
                .kind(member.getKind())
                .name(edsAsset.getName())
                .namespace(member.getNamespace())
                .custom(templateCustom.dump())
                .edsInstanceId(provider.findOf(member.getNamespace(), templateCustom)
                        .getId())
                .createdBy(createdBy)
                .build();
        resourceService.add(resource);
    }

    private KubernetesResourceTemplateCustom.Custom getCustom(
            KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate) {
        KubernetesResourceTemplateCustom.Custom userCustom = KubernetesResourceTemplateCustom.loadAs(
                createResourceByTemplate.getCustom());
        KubernetesResourceTemplate kubernetesResourceTemplate = templateService.getById(
                createResourceByTemplate.getTemplateId());
        KubernetesResourceTemplateCustom.Custom templateCustom = KubernetesResourceTemplateCustom.loadAs(
                kubernetesResourceTemplate);
        return KubernetesResourceTemplateCustom.merge(userCustom, templateCustom);
    }

}
