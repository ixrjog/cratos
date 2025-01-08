package com.baiyi.cratos.facade.kubernetes.ssh.handler;

import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.param.socket.kubernetes.KubernetesContainerTerminalParam;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.kubernetes.details.KubernetesRemoteInvokeHandler;
import com.baiyi.cratos.facade.kubernetes.ssh.BaseKubernetesSshChannelHandler;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.model.KubernetesSession;
import com.baiyi.cratos.ssh.core.model.KubernetesSessionPool;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/17 14:33
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesSshWatchLogChannelHandler extends BaseKubernetesSshChannelHandler<KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest> {

    private final KubernetesRemoteInvokeHandler kubernetesRemoteInvokeHandler;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsInstanceService edsInstanceService;
    //private final SshSessionInstanceService sshSessionInstanceService;
    private final SimpleSshSessionFacade simpleSshSessionFacade;
    private final SshAuditProperties sshAuditProperties;

    public static final int LOG_LINES = 100;

    @Override
    public String getTopic() {
        return HasTopic.APPLICATION_KUBERNETES_WATCH_LOG;
    }

    @Override
    public void handleRequest(String sessionId, Session session,
                              KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest message) throws IllegalStateException, IOException {
        if (SocketActionRequestEnum.WATCH.name()
                .equalsIgnoreCase(message.getAction())) {
            Map<Integer, EdsKubernetesConfigModel.Kubernetes> kubernetesMap = Maps.newHashMap();
            message.getDeployments()
                    .forEach(deployment -> run(sessionId, deployment, kubernetesMap));
        }
        if (SocketActionRequestEnum.CLOSE.name()
                .equalsIgnoreCase(message.getAction())) {
            Map<String, KubernetesSession> kubernetesSessionMap = KubernetesSessionPool.getBySessionId(sessionId);
            if (!CollectionUtils.isEmpty(kubernetesSessionMap)) {
                kubernetesSessionMap.forEach((instanceId, kubernetesSession) -> {
                    // 关闭会话
                    KubernetesSessionPool.closeSession(sessionId, instanceId);
                    simpleSshSessionFacade.closeSshSessionInstance(sessionId, instanceId);
                });
            }
        }
    }

    private void run(String sessionId, ApplicationKubernetesParam.DeploymentRequest deployment,
                     Map<Integer, EdsKubernetesConfigModel.Kubernetes> kubernetesMap) {
        EdsInstance edsInstance = edsInstanceService.getByName(deployment.getKubernetesClusterName());
        if (edsInstance == null) {
            return;
        }
        EdsKubernetesConfigModel.Kubernetes kubernetes = getKubernetes(kubernetesMap, edsInstance.getId());
        deployment.getPods()
                .forEach(pod -> {
                    final String sshSessionInstanceId = toInstanceId(pod);
                    final String auditPath = sshAuditProperties.generateAuditLogFilePath(sessionId,
                            sshSessionInstanceId);
                    SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sessionId, pod,
                            SshSessionInstanceTypeEnum.CONTAINER_LOG, auditPath);
                    // 记录
                    simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
                    kubernetesRemoteInvokeHandler.runWatchLog(sessionId, sshSessionInstanceId, kubernetes, pod,
                            LOG_LINES);
                });
    }

    @SuppressWarnings("unchecked")
    private EdsKubernetesConfigModel.Kubernetes getKubernetes(
            Map<Integer, EdsKubernetesConfigModel.Kubernetes> kubernetesMap, int edsInstanceId) {
        if (kubernetesMap.containsKey(edsInstanceId)) {
            return kubernetesMap.get(edsInstanceId);
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                edsInstanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        kubernetesMap.put(edsInstanceId, holder.getInstance()
                .getEdsConfigModel());
        return holder.getInstance()
                .getEdsConfigModel();
    }

    private String toInstanceId(ApplicationKubernetesParam.PodRequest pod) {
        if (StringUtils.hasText(pod.getInstanceId())) {
            return pod.getInstanceId();
        }
        return Joiner.on("#")
                .join(pod.getName(), pod.getContainer()
                        .getName());
    }

}
