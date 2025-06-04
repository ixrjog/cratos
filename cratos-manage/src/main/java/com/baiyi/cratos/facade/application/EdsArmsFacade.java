package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import jakarta.validation.constraints.NotNull;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/4 14:49
 * &#064;Version 1.0
 */
public interface EdsArmsFacade {

    KubernetesVO.ARMS makeArms(@NotNull String applicationName, @NotNull String namespace);

}
