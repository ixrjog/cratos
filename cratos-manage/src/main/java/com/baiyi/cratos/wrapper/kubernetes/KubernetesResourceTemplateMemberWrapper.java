package com.baiyi.cratos.wrapper.kubernetes;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.service.EnvService;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateMemberService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseBusinessWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/4 11:09
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE_MEMBER)
public class KubernetesResourceTemplateMemberWrapper extends BaseDataTableConverter<KubernetesResourceTemplateVO.Member, KubernetesResourceTemplateMember> implements BaseBusinessWrapper<KubernetesResourceTemplateVO.HasTemplateMembers, KubernetesResourceTemplateVO.Member> {

    private final KubernetesResourceTemplateMemberService kubernetesResourceTemplateMemberService;
    private final EnvService envService;

    @Override
    public void wrap(KubernetesResourceTemplateVO.Member vo) {
        Env uniqueKey = Env.builder()
                .envName(vo.getNamespace())
                .build();
        Env env = envService.getByUniqueKey(uniqueKey);
        vo.setSeq(env != null ? env.getSeq() : 0);
    }

    @Override
    public void decorateBusiness(KubernetesResourceTemplateVO.HasTemplateMembers biz) {
        IdentityUtils.validIdentityRun(biz.getTemplateId())
                .withTrue(() -> {
                    List<KubernetesResourceTemplateVO.Member> members = kubernetesResourceTemplateMemberService.queryMemberByTemplateId(
                                    biz.getTemplateId())
                            .stream()
                            .map(e -> {
                                KubernetesResourceTemplateVO.Member vo = convert(e);
                                delegateWrap(vo);
                                return vo;
                            })
                            .sorted(Comparator.comparingInt(KubernetesResourceTemplateVO.Member::getSeq))
                            .toList();
                    biz.setMembers(toMemberMap(members));
                });
    }

    private Map<String, List<KubernetesResourceTemplateVO.Member>> toMemberMap(
            List<KubernetesResourceTemplateVO.Member> members) {
        Map<String, List<KubernetesResourceTemplateVO.Member>> templateMembers = Maps.newHashMap();
        members.forEach(e -> {
            final String kind = e.getKind();
            if (templateMembers.containsKey(kind)) {
                templateMembers.get(kind)
                        .add(e);
            } else {
                List<KubernetesResourceTemplateVO.Member> list = Lists.newArrayList();
                list.add(e);
                templateMembers.put(kind, list);
            }
        });
        return templateMembers;
    }

}