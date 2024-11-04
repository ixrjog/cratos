package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface KubernetesResourceTemplateMemberMapper extends Mapper<KubernetesResourceTemplateMember> {

    List<KubernetesResourceTemplateMember> queryPageByParam(KubernetesResourceTemplateParam.MemberPageQuery pageQuery);

}