package com.baiyi.cratos.ssh.kubernetes.handler;

import com.baiyi.cratos.domain.annotation.TopicName;
import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.param.socket.kubernetes.KubernetesContainerTerminalParam;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.access.AccessControlFacade;
import com.baiyi.cratos.ssh.kubernetes.handler.base.BaseKubernetesWebShChannelHandler;
import com.baiyi.cratos.ssh.kubernetes.invoker.KubernetesRemoteInvoker;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.model.KubernetesSession;
import com.baiyi.cratos.ssh.core.model.KubernetesSessionPool;
import com.google.common.collect.Maps;
import jakarta.websocket.Session;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/17 14:33
 * &#064;Version 1.0
 */
@Component
@TopicName(nameOf = HasTopic.APPLICATION_KUBERNETES_POD_WATCH_LOG)
public class KubernetesWebShWatchLogChannelHandler extends BaseKubernetesWebShChannelHandler<KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest> {

    public static final int LOG_LINES = 100;

    public KubernetesWebShWatchLogChannelHandler(SimpleSshSessionFacade simpleSshSessionFacade,
                                                 KubernetesRemoteInvoker kubernetesRemoteInvokeHandler,
                                                 EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                 EdsInstanceService edsInstanceService,
                                                 SshAuditProperties sshAuditProperties,
                                                 AccessControlFacade accessControlFacade,
                                                 ApplicationService applicationService) {
        super(simpleSshSessionFacade, kubernetesRemoteInvokeHandler, edsInstanceProviderHolderBuilder,
                edsInstanceService, sshAuditProperties, accessControlFacade, applicationService);
    }

    @Override
    public void handleRequest(String sessionId, Session session,
                              KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest message) throws IllegalStateException, IOException {
        SocketActionRequestEnum action = SocketActionRequestEnum.valueOf(message.getAction());
        switch (action) {
            case SocketActionRequestEnum.WATCH -> {
                Map<Integer, EdsKubernetesConfigModel.Kubernetes> kubernetesMap = Maps.newHashMap();
                Optional.of(message)
                        .map(KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest::getDeployments)
                        .ifPresent(deployments -> deployments.forEach(
                                deployment -> run(sessionId, deployment, kubernetesMap)));
            }
            case SocketActionRequestEnum.CLOSE -> doClose(sessionId);
            default -> {
                // error action
            }
        }
    }

    protected void doClose(String sessionId) {
        Map<String, KubernetesSession> kubernetesSessionMap = KubernetesSessionPool.getBySessionId(sessionId);
        if (!CollectionUtils.isEmpty(kubernetesSessionMap)) {
            kubernetesSessionMap.forEach((instanceId, kubernetesSession) -> {
                // 关闭会话
                KubernetesSessionPool.closeSession(sessionId, instanceId);
                simpleSshSessionFacade.closeSshSessionInstance(sessionId, instanceId);
            });
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
                .forEach(pod -> run(sessionId, kubernetes, pod));
    }

    private void run(String sessionId, EdsKubernetesConfigModel.Kubernetes kubernetes,
                     ApplicationKubernetesParam.PodRequest pod) {
        final String sshSessionInstanceId = pod.getInstanceId();
        final String auditPath = sshAuditProperties.generateAuditLogFilePath(sessionId, sshSessionInstanceId);
        SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sessionId, pod,
                SshSessionInstanceTypeEnum.CONTAINER_LOG, auditPath);
        // 记录
        simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
        kubernetesRemoteInvokeHandler.invokeLogWatch(sessionId, sshSessionInstanceId, kubernetes, pod, LOG_LINES);
    }

}
