package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface KubernetesResourceMapper extends Mapper<KubernetesResource> {

    List<KubernetesResource> queryPageByParam(KubernetesResourceParam.ResourcePageQuery pageQuery);

}