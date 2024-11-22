package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.eds.EdsBusinessParam;
import com.baiyi.cratos.domain.view.eds.EdsBusinessVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 下午4:45
 * &#064;Version 1.0
 */
public class EdsBusinessFacadeTest extends BaseUnit {

    @Resource
    private EdsBusinessFacade edsBusinessFacade;

    @Test
    void test() {
        EdsBusinessParam.KubernetesInstanceResourceQuery kubernetesInstanceResourceQuery = EdsBusinessParam.KubernetesInstanceResourceQuery.builder()
                // EKS-PROD
                .instanceId(105)
                .appName("mgw-core")
                .env("prod")
                .build();
        EdsBusinessVO.KubernetesInstanceResource resource = edsBusinessFacade.queryKubernetesInstanceResource(
                kubernetesInstanceResourceQuery);
        System.out.println(resource);
    }

    @Test
    void test2() {
        EdsBusinessParam.KubernetesInstanceResourceQuery kubernetesInstanceResourceQuery = EdsBusinessParam.KubernetesInstanceResourceQuery.builder()
                // ACK-PROD
                .instanceId(101)
                .appName("merchant")
                .env("prod")
                .build();
        EdsBusinessVO.KubernetesInstanceResource resource = edsBusinessFacade.queryKubernetesInstanceResource(
                kubernetesInstanceResourceQuery);
        System.out.println(resource);
    }

}
