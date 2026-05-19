package com.baiyi.cratos.eds.kubernetes.exec;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.exec.context.PodExecContext;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
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

    public void exec(@NonNull EdsConfigs.Kubernetes kubernetes, String namespace, String podName,
                     PodExecContext execContext, CountDownLatch execLatch) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes); ExecWatch execWatch = kc.pods()
                .inNamespace(namespace)
                .withName(podName)
                .writingOutput(execContext.getOut())
                .writingError(execContext.getError())
                .usingListener(newListener(execLatch))
                .exec(execContext.toExec())) {
            boolean latchTerminationStatus = execLatch.await(execContext.getMaxWaitingTime(), TimeUnit.SECONDS);
            if (!latchTerminationStatus) {
                execContext.getError()
                        .write("Exec timeout: command did not complete within specified time".getBytes());
                execContext.setExitCode(1);
            } else {
                execContext.setExitCode(execWatch.exitCode().get());
            }
        } catch (InterruptedException ie) {
            Thread.currentThread()
                    .interrupt();
            log.warn("Interrupted while waiting for the exec: {}", ie.getMessage());
        } catch (ExecutionException executionException) {
            try {
                execContext.getError()
                        .write(executionException.getMessage()
                                       .getBytes());
            } catch (IOException ignored) {
            }
            log.warn("Execution Exception while waiting for the exec: {}", executionException.getMessage());
        } catch (IOException e) {
            log.debug("IO error during exec: {}", e.getMessage());
        }
    }

    public void exec(@NonNull EdsConfigs.Kubernetes kubernetes, String namespace, String podName, String containerName,
                     PodExecContext execContext, CountDownLatch execLatch) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes); ExecWatch execWatch = kc.pods()
                .inNamespace(namespace)
                .withName(podName)
                .inContainer(containerName)
                .writingOutput(execContext.getOut())
                .writingError(execContext.getError())
                .usingListener(newListener(execLatch))
                .exec(execContext.toExec())) {
            boolean latchTerminationStatus = execLatch.await(execContext.getMaxWaitingTime(), TimeUnit.SECONDS);
            if (!latchTerminationStatus) {
                execContext.getError()
                        .write("Exec timeout: command did not complete within specified time".getBytes());
                execContext.setExitCode(1);
            } else {
                execContext.setExitCode(execWatch.exitCode().get());
            }
        } catch (InterruptedException ie) {
            Thread.currentThread()
                    .interrupt();
            log.warn("Interrupted while waiting for the exec: {}", ie.getMessage());
        } catch (ExecutionException executionException) {
            try {
                execContext.getError()
                        .write(executionException.getMessage()
                                       .getBytes());
            } catch (IOException ignored) {
            }
            log.warn("Execution Exception while waiting for the exec: {}", executionException.getMessage());
        } catch (IOException e) {
            log.debug("IO error during exec: {}", e.getMessage());
        }
    }

    private static PodExecListener newListener(CountDownLatch execLatch) {
        return new PodExecListener(execLatch);
    }

    private record PodExecListener(CountDownLatch execLatch) implements ExecListener {

        @Override
        public void onOpen() {
            log.info("Shell was opened");
        }

        @Override
        public void onFailure(Throwable t, Response failureResponse) {
            log.warn("Exec failure: {}", t.getMessage());
            execLatch.countDown();
        }

        @Override
        public void onClose(int i, String s) {
            log.debug("Shell Closing");
            execLatch.countDown();
        }
    }

}
