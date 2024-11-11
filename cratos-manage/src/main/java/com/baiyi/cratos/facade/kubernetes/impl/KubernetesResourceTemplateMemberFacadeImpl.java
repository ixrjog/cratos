package com.baiyi.cratos.facade.kubernetes.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceTemplateMemberFacade;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceService;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateMemberService;
import com.baiyi.cratos.wrapper.kubernetes.KubernetesResourceTemplateMemberWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/4 16:15
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesResourceTemplateMemberFacadeImpl implements KubernetesResourceTemplateMemberFacade {

    private final KubernetesResourceTemplateMemberService templateMemberService;
    private final KubernetesResourceTemplateMemberWrapper templateMemberWrapper;
    private final KubernetesResourceService resourceService;

    @Override
    public DataTable<KubernetesResourceTemplateVO.Member> queryMemberPage(
            KubernetesResourceTemplateParam.MemberPageQuery pageQuery) {
        DataTable<KubernetesResourceTemplateMember> table = templateMemberService.queryMemberPage(pageQuery);
        return templateMemberWrapper.wrapToTarget(table);
    }

    @Override
    public void addMember(KubernetesResourceTemplateParam.AddMember addMember) {
        KubernetesResourceTemplateMember member = addMember.toTarget();
        KubernetesResourceKindEnum.checkKind(addMember.getKind());
        templateMemberService.add(member);
    }

    @Override
    public void updateMember(KubernetesResourceTemplateParam.UpdateMember updateMember) {
        KubernetesResourceTemplateMember member = templateMemberService.getById(updateMember.getId());
        if (member == null) {
            return;
        }
        member.setContent(updateMember.getContent());
        member.setCustom(updateMember.getCustom());
        templateMemberService.updateByPrimaryKey(member);
    }

    @Override
    public void setValidById(int id) {
        templateMemberService.updateValidById(id);
    }

    @Override
    public void deleteById(int id) {
        resourceService.queryByMemberId(id)
                .forEach(resource -> resourceService.deleteById(resource.getId()));
        templateMemberService.deleteById(id);
    }

}
