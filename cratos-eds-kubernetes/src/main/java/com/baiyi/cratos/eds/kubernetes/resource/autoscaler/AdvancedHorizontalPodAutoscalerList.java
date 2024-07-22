package com.baiyi.cratos.eds.kubernetes.resource.autoscaler;

import com.baiyi.cratos.eds.kubernetes.resource.AdvancedHorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.DefaultKubernetesResourceList;

import java.io.Serial;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/22 下午2:05
 * &#064;Version 1.0
 */
public class AdvancedHorizontalPodAutoscalerList extends DefaultKubernetesResourceList<AdvancedHorizontalPodAutoscaler> {
    @Serial
    private static final long serialVersionUID = -8697389176452360594L;
}
