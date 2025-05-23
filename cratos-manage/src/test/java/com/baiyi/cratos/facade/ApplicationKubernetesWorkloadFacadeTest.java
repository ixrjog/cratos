package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesContainerVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDetailsFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 11:05
 * &#064;Version 1.0
 */
public class ApplicationKubernetesWorkloadFacadeTest extends BaseUnit {

    @Resource
    private ApplicationKubernetesDetailsFacade applicationKubernetesWorkloadFacade;

    @Test
    void test() {
        ApplicationKubernetesParam.QueryKubernetesDetails param = ApplicationKubernetesParam.QueryKubernetesDetails.builder()
                .applicationName("scene-data-product")
                .namespace("daily")
                .build();

        MessageResponse<KubernetesVO.KubernetesDetails> response = applicationKubernetesWorkloadFacade.queryKubernetesDetails(
                param);
        System.out.println(response);
    }

    @Test
    void test2() {
        ApplicationKubernetesParam.QueryKubernetesDeploymentImageVersion query = ApplicationKubernetesParam.QueryKubernetesDeploymentImageVersion.builder()
                .image("502076313352.dkr.ecr.eu-west-1.amazonaws.com/prod/leo-demo:b15ac794-1")
                .build();
        KubernetesContainerVO.ImageVersion version = applicationKubernetesWorkloadFacade.queryKubernetesDeploymentImageVersion(
                query);
        System.out.println(version);
    }

}
