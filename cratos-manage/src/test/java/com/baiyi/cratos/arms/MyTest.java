package com.baiyi.cratos.arms;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/15 15:24
 * &#064;Version 1.0
 */
public class MyTest extends BaseEdsTest<EdsConfigs.Kubernetes> {

    /**
     * config_robi-cce-test
     */
    public static final int CONFIG_ROBI_CCE_TEST = 40;

    private static final String ENV = "daily";

    @Resource
    private KubernetesDeploymentRepo kubernetesDeploymentRepo;

    @Test
    void test() {
        EdsConfigs.Kubernetes cfg = getConfig(CONFIG_ROBI_CCE_TEST);
        List<Deployment> deployments = kubernetesDeploymentRepo.list(cfg, ENV);
        for (Deployment deployment : deployments) {
            Map<String, String> labels = Optional.of(deployment)
                    .map(Deployment::getMetadata)
                    .map(ObjectMeta::getLabels)
                    .orElse(Map.of());
            if (!labels.containsKey("app")) {
                System.out.println("没有找到 app 标签, name=" + deployment.getMetadata()
                        .getName());
                continue;
            }
            String app = labels.get("app");
            String env = ENV;
            if (app.endsWith("-" + env)) {
                app = StringFormatter.eraseLastStr(app, "-" + env);
            }
            String countrycode = "bd";
            deployment.getMetadata()
                    .getLabels()
                    .put("app", app);
            deployment.getMetadata()
                    .getLabels()
                    .put("env", env);
            deployment.getMetadata()
                    .getLabels()
                    .put("countrycode", countrycode);

            if (!deployment.getSpec()
                    .getTemplate()
                    .getMetadata()
                    .getLabels()
                    .containsKey("armsPilotAutoEnable")) {
                System.out.println("没有找到 armsPilotAutoEnable 标签, name=" + deployment.getMetadata()
                        .getName());
                deployment.getSpec()
                        .getTemplate()
                        .getMetadata()
                        .getLabels()
                        .put("armsPilotCreateAppName", countrycode + "-" + app + "-" + env);
                deployment.getSpec()
                        .getTemplate()
                        .getMetadata()
                        .getLabels()
                        .put("armsPilotAutoEnable", "on");
                System.out.println("开始修改 name=" + deployment.getMetadata()
                        .getName() + " app=" + app + " env=" + env + " countrycode=" + countrycode);
                kubernetesDeploymentRepo.update(cfg, deployment);
            }

        }
    }


}
