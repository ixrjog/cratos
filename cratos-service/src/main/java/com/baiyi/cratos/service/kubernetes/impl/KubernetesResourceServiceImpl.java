package com.baiyi.cratos.service.kubernetes.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceParam;
import com.baiyi.cratos.mapper.KubernetesResourceMapper;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/7 10:00
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class KubernetesResourceServiceImpl implements KubernetesResourceService {

    private final KubernetesResourceMapper kubernetesResourceMapper;

    @Override
    public KubernetesResource getByUniqueKey(@NonNull KubernetesResource record) {
        Example example = new Example(KubernetesResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("edsInstanceId", record.getEdsInstanceId())
                .andEqualTo("name", record.getName())
                .andEqualTo("namespace", record.getNamespace())
                .andEqualTo("kind", record.getKind());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public DataTable<KubernetesResource> queryResourcePage(KubernetesResourceParam.ResourcePageQuery pageQuery) {
        Page<KubernetesResource> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<KubernetesResource> data = kubernetesResourceMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<KubernetesResource> queryByMemberId(int id) {
        Example example = new Example(KubernetesResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberId", id);
        return getMapper().selectByExample(example);
    }

}
