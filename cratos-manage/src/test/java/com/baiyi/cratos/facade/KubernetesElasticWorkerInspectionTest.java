package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.inspection.impl.KubernetesElasticWorkerInspection;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/16 09:44
 * &#064;Version 1.0
 */
public class KubernetesElasticWorkerInspectionTest extends BaseUnit {

    @Resource
    private KubernetesElasticWorkerInspection kubernetesElasticWorkerInspection;

    @Test
    void test() {
        kubernetesElasticWorkerInspection.inspectionTask();
    }

}
