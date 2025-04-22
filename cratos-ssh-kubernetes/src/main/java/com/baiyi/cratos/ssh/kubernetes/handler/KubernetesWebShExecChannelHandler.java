package com.baiyi.cratos.ssh.kubernetes.handler;

import com.baiyi.cratos.domain.annotation.TopicName;
import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.param.socket.kubernetes.KubernetesContainerTerminalParam;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.facade.AccessControlFacade;
import com.baiyi.cratos.ssh.core.auditor.PodCommandAuditor;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.model.KubernetesSession;
import com.baiyi.cratos.ssh.core.model.KubernetesSessionPool;
import com.baiyi.cratos.ssh.kubernetes.handler.base.BaseKubernetesWebShChannelHandler;
import com.baiyi.cratos.ssh.kubernetes.invoker.KubernetesRemoteInvoker;
import com.google.common.collect.Maps;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/9 10:00
 * &#064;Version 1.0
 */
@Slf4j
@Component
@TopicName(nameOf = HasTopic.APPLICATION_KUBERNETES_POD_EXEC)
public class KubernetesWebShExecChannelHandler extends BaseKubernetesWebShChannelHandler<KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest> {

    private final PodCommandAuditor podCommandAuditor;

    public KubernetesWebShExecChannelHandler(SimpleSshSessionFacade simpleSshSessionFacade,
                                             KubernetesRemoteInvoker kubernetesRemoteInvokeHandler,
                                             EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                             EdsInstanceService edsInstanceService,
                                             SshAuditProperties sshAuditProperties, PodCommandAuditor podCommandAuditor,
                                             AccessControlFacade accessControlFacade,
                                             ApplicationService applicationService) {
        super(simpleSshSessionFacade, kubernetesRemoteInvokeHandler, edsInstanceProviderHolderBuilder,
                edsInstanceService, sshAuditProperties, accessControlFacade, applicationService);
        this.podCommandAuditor = podCommandAuditor;
    }

    @Override
    public void handleRequest(String sessionId, String username, Session session,
                              KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest message) throws IllegalStateException, IOException {
        SocketActionRequestEnum action = SocketActionRequestEnum.valueOf(message.getAction());
        switch (action) {
            case SocketActionRequestEnum.EXEC -> {
                boolean pass = accessInterception(username, message);
                log.info("accessInterception pass={}.", pass);
                if (!pass) {
                    if (session.isOpen()) {
                        session.getBasicRemote()
                                .sendText(MessageResponse.unauthorizedAccess()
                                        .toString());
                    }
                    return;
                }
                Map<Integer, EdsKubernetesConfigModel.Kubernetes> kubernetesMap = Maps.newHashMap();
                Optional.of(message)
                        .map(KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest::getDeployments)
                        .ifPresent(deployments -> deployments.forEach(
                                deployment -> run(sessionId, deployment, kubernetesMap)));
            }
            case SocketActionRequestEnum.INPUT -> Optional.of(message)
                    .map(KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest::getDeployments)
                    .ifPresent(deployments -> deployments.forEach(deployment -> doInput(sessionId, deployment)));
            case SocketActionRequestEnum.RESIZE -> Optional.of(message)
                    .map(KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest::getDeployments)
                    .ifPresent(deployments -> deployments.forEach(deployment -> doResize(sessionId, deployment)));
            case SocketActionRequestEnum.EXIT -> Optional.of(message)
                    .map(KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest::getDeployments)
                    .ifPresent(deployments -> deployments.forEach(deployment -> doExit(sessionId, deployment)));
            case SocketActionRequestEnum.CLOSE -> closeSession(sessionId);
            default -> {
                // error action
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
                    final String instanceId = pod.getInstanceId();
                    final String auditPath = sshAuditProperties.generateAuditLogFilePath(sessionId, instanceId);
                    SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sessionId, pod,
                            SshSessionInstanceTypeEnum.CONTAINER_SHELL, auditPath);
                    // 记录
                    simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
                    kubernetesRemoteInvokeHandler.invokeExecWatch(sessionId, instanceId, kubernetes, pod, auditPath);
                });
    }

    private void doInput(String sessionId, ApplicationKubernetesParam.DeploymentRequest deploymentRequest) {
        Optional.of(deploymentRequest)
                .map(ApplicationKubernetesParam.DeploymentRequest::getPods)
                .ifPresent(pods -> pods.forEach(pod -> {
                    String instanceId = pod.getInstanceId();
                    KubernetesSession kubernetesSession = KubernetesSessionPool.getBySessionId(sessionId, instanceId);
                    if (kubernetesSession != null) {
                        sendInput(kubernetesSession, pod.getInput());
                    }
                }));
    }

    private void sendInput(KubernetesSession kubernetesSession, String input) {
        try {
            OutputStream out = kubernetesSession.getExecWatch()
                    .getInput();
            out.write(input.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    private void doResize(String sessionId, ApplicationKubernetesParam.DeploymentRequest deploymentRequest) {
        Optional.of(deploymentRequest)
                .map(ApplicationKubernetesParam.DeploymentRequest::getPods)
                .ifPresent(pods -> pods.forEach(pod -> {
                    KubernetesSession kubernetesSession = KubernetesSessionPool.getBySessionId(sessionId,
                            pod.getInstanceId());
                    if (kubernetesSession != null) {
                        kubernetesSession.resize(pod);
                    }
                }));
    }

    protected void closeSession(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            return;
        }
        Map<String, KubernetesSession> kubernetesSessionMap = KubernetesSessionPool.getBySessionId(sessionId);
        if (!CollectionUtils.isEmpty(kubernetesSessionMap)) {
            kubernetesSessionMap.forEach((instanceId, kubernetesSession) -> doExit(sessionId, instanceId));
        }
        simpleSshSessionFacade.closeSshSession(sessionId);
    }

    private void doExit(String sessionId, String instanceId) {
        KubernetesSessionPool.closeSession(sessionId, instanceId);
        simpleSshSessionFacade.closeSshSessionInstance(sessionId, instanceId);
        podCommandAuditor.asyncRecordCommand(sessionId, instanceId);
    }

    private void doExit(String sessionId, ApplicationKubernetesParam.DeploymentRequest deploymentRequest) {
        Optional.of(deploymentRequest)
                .map(ApplicationKubernetesParam.DeploymentRequest::getPods)
                .ifPresent(pods -> pods.forEach(pod -> doExit(sessionId, pod.getInstanceId())));
    }

}
