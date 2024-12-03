package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.facade.application.ApplicationKubernetesWorkloadFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 11:05
 * &#064;Version 1.0
 */
public class ApplicationKubernetesWorkloadFacadeTest extends BaseUnit {

    @Resource
    private ApplicationKubernetesWorkloadFacade applicationKubernetesWorkloadFacade;

    @Test
    void test() {
        ApplicationKubernetesParam.QueryApplicationResourceKubernetesWorkload param = ApplicationKubernetesParam.QueryApplicationResourceKubernetesWorkload.builder()
                .applicationName("kili")
                .namespace("daily")
                .build();

        MessageResponse<KubernetesVO.KubernetesWorkload> response = applicationKubernetesWorkloadFacade.queryKubernetesWorkload(
                param);
        System.out.println(response);
    }

}
