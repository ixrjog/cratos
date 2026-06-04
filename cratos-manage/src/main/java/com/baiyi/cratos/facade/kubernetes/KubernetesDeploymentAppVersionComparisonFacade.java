package com.baiyi.cratos.facade.kubernetes;

import com.baiyi.cratos.domain.view.kubernetes.KubernetesDeploymentAppVersionVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/3 17:09
 * &#064;Version 1.0
 */
public interface KubernetesDeploymentAppVersionComparisonFacade {

    KubernetesDeploymentAppVersionVO.ComparisonVersion compareDeploymentVersion(int id);

}
