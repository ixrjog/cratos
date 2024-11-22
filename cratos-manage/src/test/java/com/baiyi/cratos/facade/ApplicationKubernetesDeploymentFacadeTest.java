package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.MessageResponse;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDeploymentFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 11:05
 * &#064;Version 1.0
 */
public class ApplicationKubernetesDeploymentFacadeTest extends BaseUnit {

    @Resource
    private ApplicationKubernetesDeploymentFacade applicationKubernetesDeploymentFacade;

    @Test
    void test() {
        ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails param = ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails.builder()
                .applicationName("kili")
                .namespace("daily")
                .build();

        MessageResponse<KubernetesVO.KubernetesDetails> response = applicationKubernetesDeploymentFacade.queryKubernetesDeploymentDetails(
                param);
        System.out.println(response);
    }

}
