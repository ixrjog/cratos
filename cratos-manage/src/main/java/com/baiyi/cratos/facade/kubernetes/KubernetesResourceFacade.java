package com.baiyi.cratos.facade.kubernetes;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/7 10:22
 * &#064;Version 1.0
 */
public interface KubernetesResourceFacade {

    DataTable<KubernetesResourceVO.Resource> queryResourcePage(
            KubernetesResourceParam.ResourcePageQuery pageQuery);

    void deleteById(int id);

}
