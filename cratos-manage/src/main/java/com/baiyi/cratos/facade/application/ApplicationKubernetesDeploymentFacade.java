package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.common.MessageResponse;
import com.baiyi.cratos.domain.param.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.ApplicationResourceKubernetesVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:38
 * &#064;Version 1.0
 */
public interface ApplicationKubernetesDeploymentFacade {

    MessageResponse<ApplicationResourceKubernetesVO.KubernetesDetails> queryKubernetesDeploymentDetails(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails queryApplicationKubernetesDeployment);

}
