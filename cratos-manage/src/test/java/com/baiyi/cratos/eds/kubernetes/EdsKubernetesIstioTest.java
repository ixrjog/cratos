package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIngressRepo;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/21 上午9:55
 * &#064;Version 1.0
 */
public class EdsKubernetesIstioTest extends BaseEdsTest<EdsKubernetesConfigModel.Kubernetes> {

    public static final int CONFIG_ACK_DEV = 10;

    public static final int CONFIG_ACK_DAILY = 11;

    public static final int CONFIG_ACK_SIT = 12;

    public static final int CONFIG_ACK_PRE = 13;

    @Resource
    private KubernetesIngressRepo kubernetesIngressRepo;

    private static final String ALB_INGRESS_KUBERNETES_IO_BACKEND_KEEPALIVE = "alb.ingress.kubernetes.io/backend-keepalive";

    @Test
    void ingressDevTest() {
        updateIngress(CONFIG_ACK_DEV, "dev");
    }

    @Test
    void ingressDailyTest() {
        updateIngress(CONFIG_ACK_DAILY, "daily");
    }

    @Test
    void ingressSitTest() {
        updateIngress(CONFIG_ACK_SIT, "sit");
    }

    @Test
    void ingressPreTest() {
        updateIngress(CONFIG_ACK_PRE, "pre");
    }

    /**
     * ACK Ingress 启用长链接
     *
     * @param configId
     * @param namespace
     */
    private void updateIngress(int configId, String namespace) {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(configId);
        List<Ingress> ingressList = kubernetesIngressRepo.list(cfg, namespace);
        for (Ingress ingress : ingressList) {
            if (!ingress.getMetadata()
                    .getAnnotations()
                    .containsKey(ALB_INGRESS_KUBERNETES_IO_BACKEND_KEEPALIVE)) {
                ingress.getMetadata()
                        .getAnnotations()
                        .put(ALB_INGRESS_KUBERNETES_IO_BACKEND_KEEPALIVE, "true");
                kubernetesIngressRepo.update(cfg, ingress);
                String msg = "Ingress {} enable backend-keepalive.";
                System.out.println(StringFormatter.arrayFormat(msg, ingress.getMetadata()
                        .getName()));
            }
        }
    }

    @Test
    void test() {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(104 , EdsAssetTypeEnum.KUBERNETES_INGRESS.name());
        List<Ingress> ingressList = kubernetesIngressRepo.list(cfg, "dev");
        for (Ingress ingress : ingressList) {
            // 打印Ingress注解配置
            String msg = "Ingress {} annotations: {}";
            System.out.println(StringFormatter.arrayFormat(msg, ingress.getMetadata()
                    .getName(), ingress.getMetadata()
                    .getAnnotations()));
        }
    }

}
