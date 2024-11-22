package com.baiyi.cratos.service.kubernetes;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceParam;
import com.baiyi.cratos.mapper.KubernetesResourceMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/7 10:00
 * &#064;Version 1.0
 */
public interface KubernetesResourceService extends BaseUniqueKeyService<KubernetesResource, KubernetesResourceMapper>, BaseService<KubernetesResource, KubernetesResourceMapper> {

    DataTable<KubernetesResource> queryResourcePage(KubernetesResourceParam.ResourcePageQuery pageQuery);

    List<KubernetesResource> queryByMemberId(int id);

}
