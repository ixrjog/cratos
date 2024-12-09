package com.baiyi.cratos.notification;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.inspection.impl.KubernetesElasticWorkerInspection;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/9 11:00
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
