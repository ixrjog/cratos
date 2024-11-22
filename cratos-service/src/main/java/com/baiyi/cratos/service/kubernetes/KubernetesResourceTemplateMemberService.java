package com.baiyi.cratos.service.kubernetes;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.mapper.KubernetesResourceTemplateMemberMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/4 10:12
 * &#064;Version 1.0
 */
public interface KubernetesResourceTemplateMemberService extends BaseUniqueKeyService<KubernetesResourceTemplateMember, KubernetesResourceTemplateMemberMapper>, BaseValidService<KubernetesResourceTemplateMember, KubernetesResourceTemplateMemberMapper> {

    DataTable<KubernetesResourceTemplateMember> queryMemberPage(KubernetesResourceTemplateParam.MemberPageQuery pageQuery);

    List<KubernetesResourceTemplateMember> queryMemberByTemplateId(int templateId, boolean valid);

    List<KubernetesResourceTemplateMember> queryMemberByTemplateId(int templateId);

}

