package com.baiyi.cratos.facade.inspection.model;

import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/9 10:28
 * &#064;Version 1.0
 */
public class KubernetesElasticWorkerModel {

    @Data
    @Builder
    public static class Worker {
        String instanceName;
        String name;
    }

}
