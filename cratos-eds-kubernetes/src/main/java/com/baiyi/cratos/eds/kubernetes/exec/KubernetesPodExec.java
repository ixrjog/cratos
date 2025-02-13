package com.baiyi.cratos.eds.kubernetes.exec;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.exec.context.PodExecContext;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 09:54
 * &#064;Version 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesPodExec {

    private final KubernetesClientBuilder kubernetesClientBuilder;
    private static final CountDownLatch execLatch = new CountDownLatch(1);

    public void exec(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String podName,
                     String containerName, PodExecContext execContext) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes); ExecWatch execWatch = kc.pods()
                .inNamespace(namespace)
                .withName(podName)
                // 如果Pod中只有一个容器，不需要指定
                .inContainer(containerName)
                .writingOutput(execContext.getOut())
                .writingError(execContext.getError())
                .usingListener(newListener())
                .exec(execContext.getExec())) {
            boolean latchTerminationStatus = execLatch.await(5, TimeUnit.SECONDS);
            if (!latchTerminationStatus) {
                log.warn("Latch could not terminate within specified time");
            }
            log.info("Exec Output: {} ", execContext.getOut());
        } catch (InterruptedException ie) {
            Thread.currentThread()
                    .interrupt();
            log.warn("Interrupted while waiting for the exec: {}", ie.getMessage());
        }
    }

    private static PodExecListener newListener() {
        return new PodExecListener();
    }

    private static class PodExecListener implements ExecListener {

        @Override
        public void onOpen() {
            log.info("Shell was opened");
        }

        @Override
        public void onFailure(Throwable t, Response failureResponse) {
            log.info("Some error encountered");
            execLatch.countDown();
        }

        @Override
        public void onClose(int i, String s) {
            log.info("Shell Closing");
            execLatch.countDown();
        }

    }
}
