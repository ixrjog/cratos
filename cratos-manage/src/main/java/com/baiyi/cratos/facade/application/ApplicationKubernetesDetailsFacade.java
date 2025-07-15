package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesContainerVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:38
 * &#064;Version 1.0
 */
public interface ApplicationKubernetesDetailsFacade {

    MessageResponse<KubernetesVO.KubernetesDetails> queryKubernetesDetails(
            ApplicationKubernetesParam.QueryKubernetesDetails queryKubernetesDetails);

    OptionsVO.Options queryKubernetesDeploymentOptions(
            ApplicationKubernetesParam.QueryKubernetesDeploymentOptions queryKubernetesDeploymentOptions);

    KubernetesContainerVO.ImageVersion queryKubernetesDeploymentImageVersion(
            ApplicationKubernetesParam.QueryKubernetesDeploymentImageVersion queryKubernetesDeploymentImageVersion);

    void deleteApplicationResourceKubernetesDeploymentPod(
            ApplicationKubernetesParam.DeleteApplicationResourceKubernetesDeploymentPod parm);

    void redeployApplicationResourceKubernetesDeployment(
            ApplicationKubernetesParam.RedeployApplicationResourceKubernetesDeployment param);

}
