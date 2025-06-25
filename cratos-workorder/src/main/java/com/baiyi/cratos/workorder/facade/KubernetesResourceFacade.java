package com.baiyi.cratos.workorder.facade;

import com.baiyi.cratos.domain.model.ApplicationModel;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/24 18:08
 * &#064;Version 1.0
 */
public interface KubernetesResourceFacade {

    void createKubernetesResource(ApplicationModel.CreateFrontEndApplication createFrontEndApplication);

}
