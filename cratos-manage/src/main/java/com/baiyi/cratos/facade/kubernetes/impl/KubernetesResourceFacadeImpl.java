package com.baiyi.cratos.facade.kubernetes.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesResourceFacade;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceService;
import com.baiyi.cratos.wrapper.kubernetes.KubernetesResourceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/7 10:22
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesResourceFacadeImpl implements KubernetesResourceFacade {

    private final KubernetesResourceService resourceService;
    private final KubernetesResourceWrapper resourceWrapper;

    @Override
    public DataTable<KubernetesResourceVO.Resource> queryResourcePage(
            KubernetesResourceParam.ResourcePageQuery pageQuery) {
        DataTable<KubernetesResource> table = resourceService.queryResourcePage(pageQuery);
        return resourceWrapper.wrapToTarget(table);
    }

    @Override
    public void deleteById(int id) {
        // TODO 是否要删除资产?
        resourceService.deleteById(id);
    }

}
