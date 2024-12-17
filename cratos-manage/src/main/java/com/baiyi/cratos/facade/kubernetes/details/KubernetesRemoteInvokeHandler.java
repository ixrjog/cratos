package com.baiyi.cratos.facade.kubernetes.details;

import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesPodRepo;
import com.baiyi.cratos.ssh.core.model.KubernetesSession;
import com.baiyi.cratos.ssh.core.model.KubernetesSessionPool;
import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.watch.kubernetes.WatchKubernetesTerminalOutputTask;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/17 15:10
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesRemoteInvokeHandler {

    private final KubernetesPodRepo kubernetesPodRepo;

    public void runWatchLog(String sessionId, String instanceId, EdsKubernetesConfigModel.Kubernetes kubernetes,
                                   ApplicationKubernetesParam.PodRequest pod, Integer lines) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LogWatch logWatch = kubernetesPodRepo.getLogWatch(kubernetes,
                pod.getNamespace(),
                pod.getName(),
                pod.getContainer().getName(),
                lines, out);
        SessionOutput sessionOutput = new SessionOutput(sessionId, instanceId);
        // 启动线程处理会话
        WatchKubernetesTerminalOutputTask run = new WatchKubernetesTerminalOutputTask(sessionOutput, out);
        // JDK21 VirtualThreads
        Thread.ofVirtual().start(run);
        KubernetesSession kubernetesSession = KubernetesSession.builder()
                .sessionId(sessionId)
                .instanceId(instanceId)
                .logWatch(logWatch)
                .watchKubernetesTerminalOutputTask(run)
                .build();
        kubernetesSession.setSessionOutput(sessionOutput);
        KubernetesSessionPool.addSession(kubernetesSession);
    }

}
