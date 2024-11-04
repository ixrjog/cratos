package com.baiyi.cratos.service.kubernetes.impl;

import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.mapper.KubernetesResourceTemplateMemberMapper;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateMemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/4 10:12
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class KubernetesResourceTemplateMemberServiceImpl implements KubernetesResourceTemplateMemberService {

    private final KubernetesResourceTemplateMemberMapper kubernetesResourceTemplateMemberMapper;

    @Override
    public KubernetesResourceTemplateMember getByUniqueKey(@NonNull KubernetesResourceTemplateMember record) {
        Example example = new Example(KubernetesResourceTemplateMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", record.getTemplateId())
                .andEqualTo("namespace", record.getNamespace());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public List<KubernetesResourceTemplateMember> queryMemberByTemplateId(int templateId, boolean valid) {
        Example example = new Example(KubernetesResourceTemplateMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", templateId)
                .andEqualTo("valid", valid);
        return getMapper().selectByExample(example);
    }

    @Override
    public List<KubernetesResourceTemplateMember> queryMemberByTemplateId(int templateId) {
        Example example = new Example(KubernetesResourceTemplateMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", templateId);
        return getMapper().selectByExample(example);
    }

}

