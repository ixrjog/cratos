package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.util.KubernetesResourceUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/12 17:39
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
public class EdsKubernetes2Test extends BaseEdsTest<EdsConfigs.Kubernetes> {

    // BD-DHK-CCE-PROD 147
    // BD-JSR-CCE-PROD 129
    // PK-ISB-CCE-PROD 136
    // PK-LHR-CCE-PROD 140

    @Resource
    private KubernetesResourceUtils kubernetesResourceUtils;

    @Test
    void test1() {
        int instanceId = 140;
        System.out.println(kubernetesResourceUtils.printKubernetesResourceTable(instanceId));
    }

}
