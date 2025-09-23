package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import io.fabric8.kubernetes.api.model.apps.Deployment;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/23 14:22
 * &#064;Version 1.0
 */
public interface ApplicationKubernetesDeploymentFacade {

    Deployment queryApplicationResourceKubernetesDeployment(
            ApplicationKubernetesParam.QueryKubernetesDeployment queryKubernetesDeployment);

}
