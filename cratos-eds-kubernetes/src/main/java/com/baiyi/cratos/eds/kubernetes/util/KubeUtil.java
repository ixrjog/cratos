package com.baiyi.cratos.eds.kubernetes.util;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/4/23 下午3:41
 * @Version 1.0
 */
public class KubeUtil {

    private KubeUtil() {
    }

    public static int getReplicas(Deployment deployment) {
        return Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElse(0);
    }

    public static void setReplicas(Deployment deployment, int replicas) {
        deployment.getSpec()
                .setReplicas(replicas);
    }

}
