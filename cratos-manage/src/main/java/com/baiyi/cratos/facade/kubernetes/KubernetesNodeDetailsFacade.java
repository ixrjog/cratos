package com.baiyi.cratos.facade.kubernetes;

import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.eds.EdsKubernetesNodeParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/19 11:41
 * &#064;Version 1.0
 */
public interface KubernetesNodeDetailsFacade {

    MessageResponse<KubernetesNodeVO.KubernetesNodeDetails> queryEdsKubernetesNodeDetails(
            EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails kubernetesNodeDetailsRequest);

}
