package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface KubernetesResourceMapper extends Mapper<KubernetesResource> {

    List<KubernetesResource> queryPageByParam(KubernetesResourceParam.ResourcePageQuery pageQuery);

}