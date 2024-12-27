package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.eds.EdsKubernetesNodeParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesNodeDetailsFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/19 15:01
 * &#064;Version 1.0
 */
public class KubernetesNodeDetailsFacadeTest extends BaseUnit {

    @Resource
    private KubernetesNodeDetailsFacade kubernetesNodeDetailsFacade;

    @Test
    void test() {
        EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails queryEdsKubernetesNodeDetails = EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails.builder()
                .instanceName("ACK-PROD")
                .build();
        MessageResponse<KubernetesNodeVO.KubernetesNodeDetails> response = kubernetesNodeDetailsFacade.queryEdsKubernetesNodeDetails(
                queryEdsKubernetesNodeDetails);
        System.out.println(response);
    }

}
