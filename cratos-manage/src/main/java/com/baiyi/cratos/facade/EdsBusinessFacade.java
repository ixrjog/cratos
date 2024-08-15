package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.eds.EdsBusinessParam;
import com.baiyi.cratos.domain.view.eds.EdsBusinessVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 下午3:35
 * &#064;Version 1.0
 */
public interface EdsBusinessFacade {

    EdsBusinessVO.KubernetesInstanceResource queryKubernetesInstanceResource(
            final EdsBusinessParam.KubernetesInstanceResourceQuery kubernetesInstanceResourceQuery);

}
