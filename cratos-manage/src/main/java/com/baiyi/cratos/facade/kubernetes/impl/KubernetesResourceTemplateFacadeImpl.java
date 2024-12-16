package com.baiyi.cratos.facade.kubernetes.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateFacade;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateMemberFacade;
import com.baiyi.cratos.facade.kubernetes.provider.KubernetesResourceProvider;
import com.baiyi.cratos.facade.kubernetes.provider.factory.KubernetesResourceProviderFactory;
import com.baiyi.cratos.facade.kubernetes.util.KubernetesResourceBuilder;
import com.baiyi.cratos.facade.kubernetes.util.KubernetesResourceTemplateBuilder;
import com.baiyi.cratos.facade.kubernetes.util.KubernetesResourceTemplateMemberBuilder;
import com.baiyi.cratos.facade.kubernetes.util.TemplateCustomMerger;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceService;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateMemberService;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateService;
import com.baiyi.cratos.wrapper.kubernetes.KubernetesResourceTemplateWrapper;
import com.google.api.client.util.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    private static final boolean LOCKED = true;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE)
    public DataTable<KubernetesResourceTemplateVO.Template> queryTemplatePage(
            KubernetesResourceTemplateParam.TemplatePageQuery pageQuery) {
        DataTable<KubernetesResourceTemplate> table = templateService.queryTemplatePage(pageQuery.toParam());
        return templateWrapper.wrapToTarget(table);
    }

    @Override
    public void lockTemplate(KubernetesResourceTemplateParam.LockTemplate lockTemplate) {
        KubernetesResourceTemplate template = templateService.getById(lockTemplate.getTemplateId());
        if (template == null) {
            KubernetesResourceTemplateException.runtime("Template does not exist.");
        }
        template.setLocked(lockTemplate.getLocked());
        templateService.updateByPrimaryKey(template);
    }

    @Override
    public KubernetesResourceTemplateVO.Template getTemplateById(int id) {
        KubernetesResourceTemplate template = templateService.getById(id);
        if (template == null) {
            KubernetesResourceTemplateException.runtime("Template does not exist.");
        }
        return templateWrapper.wrapToTarget(template);
    }

    @Override
    public void addTemplate(KubernetesResourceTemplateParam.AddTemplate addTemplate) {
        KubernetesResourceTemplate kubernetesResourceTemplate = addTemplate.toTarget();
        KubernetesResourceTemplateCustom.loadAs(addTemplate.getCustom());
        templateService.add(kubernetesResourceTemplate);
    }

    @Override
    public void updateTemplate(KubernetesResourceTemplateParam.UpdateTemplate updateTemplate) {
        KubernetesResourceTemplate kubernetesResourceTemplate = templateService.getById(updateTemplate.getId());
        if (kubernetesResourceTemplate == null) {
            return;
        }
        if (kubernetesResourceTemplate.getLocked() == LOCKED) {
            KubernetesResourceTemplateException.runtime("Template is locked and cannot be modified.");
        }
        kubernetesResourceTemplate.setName(updateTemplate.getName());
        kubernetesResourceTemplate.setApiVersion(updateTemplate.getApiVersion());
        kubernetesResourceTemplate.setCustom(updateTemplate.getCustom());
        KubernetesResourceTemplateCustom.loadAs(updateTemplate.getCustom());
        templateService.updateByPrimaryKey(kubernetesResourceTemplate);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return templateService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        KubernetesResourceTemplate kubernetesResourceTemplate = templateService.getById(id);
        if (kubernetesResourceTemplate == null) {
            return;
        }
        if (kubernetesResourceTemplate.getLocked() == LOCKED) {
            KubernetesResourceTemplateException.runtime("Template is locked and cannot be modified.");
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
        KubernetesResourceTemplate newTemplate = KubernetesResourceTemplateBuilder.newBuilder()
                .withCopyTemplate(copyTemplate)
                .withTemplate(kubernetesResourceTemplate)
                .copy();
        templateService.add(newTemplate);
        // copy members
        copyTemplateMembers(copyTemplate, newTemplate);
        return this.getTemplateById(copyTemplate.getTemplateId());
    }

    private void copyTemplateMembers(KubernetesResourceTemplateParam.CopyTemplate copyTemplate,
                                     KubernetesResourceTemplate newTemplate) {
        List<KubernetesResourceTemplateMember> members = templateMemberService.queryMemberByTemplateId(
                copyTemplate.getTemplateId(), true);
        if (!CollectionUtils.isEmpty(members)) {
            for (KubernetesResourceTemplateMember member : members) {
                templateMemberService.add(KubernetesResourceTemplateMemberBuilder.newBuilder()
                        .withMember(member)
                        .withTemplate(newTemplate)
                        .copy());
            }
        }
    }

    @Override
    @SetSessionUserToParam(desc = "set CreatedBy")
    public void createResourceByTemplate(
            KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate) {
        if (!IdentityUtil.hasIdentity(createResourceByTemplate.getTemplateId())) {
            if (StringUtils.hasText(createResourceByTemplate.getTemplateKey())) {
                KubernetesResourceTemplate uniqueKey = KubernetesResourceTemplate.builder()
                        .templateKey(createResourceByTemplate.getTemplateKey())
                        .build();
                KubernetesResourceTemplate template = templateService.getByUniqueKey(uniqueKey);
                if (template == null) {
                    KubernetesResourceTemplateException.runtime("Template does not exist.");
                }
                createResourceByTemplate.setTemplateId(template.getId());
            } else {
                KubernetesResourceTemplateException.runtime("Template ID or Key must be specified.");
            }
        }
        KubernetesResourceTemplateCustom.Custom templateCustom = getCustomFromUserMergeTemplate(
                createResourceByTemplate);
        // 过滤实例
        templateCustom.setInstances(templateCustom.getInstances()
                .stream()
                .filter(e -> createResourceByTemplate.getInstances()
                        .contains(e.getId()))
                .toList());
        List<KubernetesResourceTemplateMember> members = queryMembers(createResourceByTemplate);
        if (!CollectionUtils.isEmpty(members)) {
            String createdBy = createResourceByTemplate.getCreatedBy();
            for (KubernetesResourceTemplateMember member : members) {
                createResourceByTemplateMember(member, templateCustom, createdBy);
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
                .withMember(member)
                .merge()
                // 策略工厂重写变量
                .rewrite()
                .get();
        // 核心处里逻辑
        KubernetesResourceProvider<?> provider = KubernetesResourceProviderFactory.getProvider(member.getKind());
        List<EdsAsset> assets = provider.produce(member, memberCustom);
        assets.forEach(asset -> {
            // 资产关联
            KubernetesResource resource = KubernetesResourceBuilder.newBuilder()
                    .withMember(member)
                    .withEdsAsset(asset)
                    .withMemberCustom(memberCustom)
                    .withCreatedBy(createdBy)
                    .get();
            resourceService.add(resource);
        });
    }

    /**
     * 获取模板的Custom
     *
     * @param createResourceByTemplate
     * @return
     */
    private KubernetesResourceTemplateCustom.Custom getCustomFromUserMergeTemplate(
            KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate) {
        // 用户的
        KubernetesResourceTemplateCustom.Custom userCustom = KubernetesResourceTemplateCustom.loadAs(
                createResourceByTemplate.getCustom());
        KubernetesResourceTemplate kubernetesResourceTemplate = templateService.getById(
                createResourceByTemplate.getTemplateId());
        // 模板的
        KubernetesResourceTemplateCustom.Custom templateCustom = KubernetesResourceTemplateCustom.loadAs(
                kubernetesResourceTemplate);
        return TemplateCustomMerger.newBuilder()
                .mergeFrom(userCustom)
                .mergeTo(templateCustom)
                .merge()
                .get();
    }

}
