package com.baiyi.cratos.eds.kubernetes.resource;

import com.baiyi.cratos.eds.kubernetes.resource.autoscaler.AdvancedHorizontalPodAutoscalerSpec;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

import java.io.Serial;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/22 下午1:52
 * &#064;Version 1.0
 */
@Version(AdvancedHorizontalPodAutoscaler.VERSION)
@Group(AdvancedHorizontalPodAutoscaler.GROUP)
public class AdvancedHorizontalPodAutoscaler extends CustomResource<AdvancedHorizontalPodAutoscalerSpec, Void> implements Namespaced {

    @Serial
    private static final long serialVersionUID = 6747132942568070503L;

    // "autoscaling.alibabacloud.com/v1beta1";
    public static final String GROUP = "autoscaling.alibabacloud.com";
    public static final String VERSION = "v1beta1";

}
