package com.baiyi.cratos.facade.kubernetes.ssh.handler;

import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.param.socket.kubernetes.KubernetesContainerTerminalParam;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.kubernetes.details.KubernetesRemoteInvokeHandler;
import com.baiyi.cratos.facade.kubernetes.ssh.BaseKubernetesWebShChannelHandler;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.model.KubernetesSession;
import com.baiyi.cratos.ssh.core.model.KubernetesSessionPool;
import com.google.common.collect.Maps;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
public class KubernetesWebShExecChannelHandler extends BaseKubernetesWebShChannelHandler<KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest> {

    public KubernetesWebShExecChannelHandler(SimpleSshSessionFacade simpleSshSessionFacade,
                                             KubernetesRemoteInvokeHandler kubernetesRemoteInvokeHandler,
                                             EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                             EdsInstanceService edsInstanceService,
                                             SshAuditProperties sshAuditProperties) {
        super(simpleSshSessionFacade, kubernetesRemoteInvokeHandler, edsInstanceProviderHolderBuilder,
                edsInstanceService, sshAuditProperties);
    }

    @Override
    public String getTopic() {
        return HasTopic.APPLICATION_KUBERNETES_POD_EXEC;
    }

    @Override
    public void handleRequest(String sessionId, Session session,
                              KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest message) throws IllegalStateException, IOException {
        if (SocketActionRequestEnum.EXEC.name()
                .equalsIgnoreCase(message.getAction())) {
            Map<Integer, EdsKubernetesConfigModel.Kubernetes> kubernetesMap = Maps.newHashMap();
            Optional.of(message)
                    .map(KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest::getDeployments)
                    .ifPresent(deployments -> deployments.forEach(
                            deployment -> run(sessionId, deployment, kubernetesMap)));
        }
        if (SocketActionRequestEnum.RESIZE.name()
                .equalsIgnoreCase(message.getAction())) {
            Optional.of(message)
                    .map(KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest::getDeployments)
                    .ifPresent(deployments -> deployments.forEach(deployment -> doResize(sessionId, deployment)));
        }
        if (SocketActionRequestEnum.EXIT.name()
                .equalsIgnoreCase(message.getAction())) {
            Optional.of(message)
                    .map(KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest::getDeployments)
                    .ifPresent(deployments -> deployments.forEach(deployment -> doExit(sessionId, deployment)));
        }
        if (SocketActionRequestEnum.CLOSE.name()
                .equalsIgnoreCase(message.getAction())) {
            doClose(sessionId);
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
                    final String auditPath = sshAuditProperties.generateAuditLogFilePath(sessionId,
                            instanceId);
                    SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sessionId, pod,
                            SshSessionInstanceTypeEnum.CONTAINER_SHELL, auditPath);
                    // 记录
                    simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
                    kubernetesRemoteInvokeHandler.invokeExecWatch(sessionId, instanceId, kubernetes, pod);
                });
    }

    private void doInput(String sessionId, ApplicationKubernetesParam.DeploymentRequest deploymentRequest) {
        Optional.of(deploymentRequest)
                .map(ApplicationKubernetesParam.DeploymentRequest::getPods)
                .ifPresent(pods -> pods.forEach(pod -> {
                    if (!StringUtils.hasText(pod.getInput())) {
                        return;
                    }
                    String instanceId = pod.getInstanceId();
                    KubernetesSession kubernetesSession = KubernetesSessionPool.getBySessionId(sessionId,
                            pod.getInstanceId());
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

    private void doExit(String sessionId, ApplicationKubernetesParam.DeploymentRequest deploymentRequest) {
        Optional.of(deploymentRequest)
                .map(ApplicationKubernetesParam.DeploymentRequest::getPods)
                .ifPresent(pods -> pods.forEach(pod -> {
                    String instanceId = pod.getInstanceId();
                    KubernetesSessionPool.closeSession(sessionId, instanceId);
                    simpleSshSessionFacade.closeSshSessionInstance(sessionId, instanceId);
                    // TODO audit recode
                }));
    }

}
