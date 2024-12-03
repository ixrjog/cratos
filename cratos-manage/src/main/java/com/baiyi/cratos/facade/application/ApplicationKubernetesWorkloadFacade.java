package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:38
 * &#064;Version 1.0
 */
public interface ApplicationKubernetesWorkloadFacade {

    MessageResponse<KubernetesVO.KubernetesWorkload> queryKubernetesWorkload(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesWorkload queryApplicationKubernetesDeployment);

}
