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
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateMemberFacade;
import com.baiyi.cratos.facade.kubernetes.provider.KubernetesResourceProvider;
import com.baiyi.cratos.facade.kubernetes.provider.factory.KubernetesResourceProviderFactory;
import com.baiyi.cratos.facade.kubernetes.util.KubernetesResourceBuilder;
import com.baiyi.cratos.facade.kubernetes.util.TemplateCustomMerger;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceService;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateMemberService;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateService;
import com.baiyi.cratos.wrapper.kubernetes.KubernetesResourceTemplateWrapper;
import com.google.api.client.util.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

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
    private final KubernetesResourceTemplateMemberFacade templateMemberFacade;

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
                .forEach(member -> templateMemberFacade.deleteById(member.getId()));
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
                member.setId(null);
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
        List<KubernetesResourceTemplateMember> members = queryMembers(createResourceByTemplate);
        if (!CollectionUtils.isEmpty(members)) {
            for (KubernetesResourceTemplateMember member : members) {
                createResourceByTemplateMember(member, templateCustom, createResourceByTemplate.getCreatedBy());
            }
        }
    }

    private List<KubernetesResourceTemplateMember> queryMembers(
            KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate) {
        List<KubernetesResourceTemplateMember> members = templateMemberService.queryMemberByTemplateId(
                createResourceByTemplate.getTemplateId(), true);
        if (CollectionUtils.isEmpty(members)) {
            return members;
        }
        Set<String> namespaces = CollectionUtils.isEmpty(
                createResourceByTemplate.getNamespaces()) ? Sets.newHashSet() : createResourceByTemplate.getNamespaces();
        Set<String> kinds = CollectionUtils.isEmpty(
                createResourceByTemplate.getKinds()) ? Sets.newHashSet() : createResourceByTemplate.getKinds();
        return members.stream()
                .filter(member -> namespaces.contains(member.getNamespace()) && kinds.contains(member.getKind()))
                .toList();
    }

    private void createResourceByTemplateMember(KubernetesResourceTemplateMember member,
                                                KubernetesResourceTemplateCustom.Custom templateCustom,
                                                String createdBy) {
        KubernetesResourceTemplateCustom.Custom mainCustom = KubernetesResourceTemplateCustom.loadAs(
                member.getCustom());
        KubernetesResourceTemplateCustom.Custom memberCustom = TemplateCustomMerger.newBuilder()
                .mergeFrom(templateCustom)
                .mergeTo(mainCustom)
                .member(member)
                .merge()
                // 策略工厂重写变量
                .build();
        KubernetesResourceProvider<?> provider = KubernetesResourceProviderFactory.getProvider(member.getKind());
        List<EdsAsset> assets = provider.produce(member, memberCustom);
        assets.forEach(asset -> {
            // 资产关联
            KubernetesResource resource = KubernetesResourceBuilder.newBuilder()
                    .member(member)
                    .edsAsset(asset)
                    .memberCustom(memberCustom)
                    .createdBy(createdBy)
                    .build();
            resourceService.add(resource);
        });
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
