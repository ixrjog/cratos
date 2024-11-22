package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceTemplateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface KubernetesResourceTemplateMapper extends Mapper<KubernetesResourceTemplate> {

    List<KubernetesResourceTemplate> queryPageByParam(KubernetesResourceTemplateParam.TemplatePageQueryParam param);

}