package com.baiyi.cratos.ssh.kubernetes.handler;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.common.util.UserDisplayUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.annotation.TopicName;
import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.env.EnvParam;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.param.socket.kubernetes.KubernetesContainerTerminalParam;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobotModel;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.AccessControlFacade;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.service.*;
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
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.KUBERNETES_WSH_EXEC_NOTICE;

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
    private final EdsInstanceHelper edsInstanceHelper;
    private final EdsConfigService edsConfigService;
    private final DingtalkService dingtalkService;
    private final NotificationTemplateService notificationTemplateService;
    private final UserService userService;
    private final EnvFacade envFacade;
    @Value("${cratos.language:en-us}")
    protected String language;

    public KubernetesWebShExecChannelHandler(SimpleSshSessionFacade simpleSshSessionFacade,
                                             KubernetesRemoteInvoker kubernetesRemoteInvokeHandler,
                                             EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                             EdsInstanceService edsInstanceService,
                                             SshAuditProperties sshAuditProperties, PodCommandAuditor podCommandAuditor,
                                             AccessControlFacade accessControlFacade,
                                             ApplicationService applicationService, EdsInstanceHelper edsInstanceHelper,
                                             EdsConfigService edsConfigService, DingtalkService dingtalkService,
                                             NotificationTemplateService notificationTemplateService,
                                             UserService userService, EnvFacade envFacade) {
        super(simpleSshSessionFacade, kubernetesRemoteInvokeHandler, edsInstanceProviderHolderBuilder,
                edsInstanceService, sshAuditProperties, accessControlFacade, applicationService);
        this.podCommandAuditor = podCommandAuditor;
        this.edsInstanceHelper = edsInstanceHelper;
        this.edsConfigService = edsConfigService;
        this.dingtalkService = dingtalkService;
        this.notificationTemplateService = notificationTemplateService;
        this.userService = userService;
        this.envFacade = envFacade;
    }

    @Override
    public void handleRequest(String sessionId, String username, Session session,
                              KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest message) throws IllegalStateException, IOException {
        log.info("handleRequest topic={}, sessionId={}, username={}", message.getTopic(), sessionId, username);
        SocketActionRequestEnum action = SocketActionRequestEnum.valueOf(message.getAction());
        switch (action) {
            case SocketActionRequestEnum.EXEC -> {
                boolean pass = accessInterception(username, message);
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
                                deployment -> run(sessionId, username, deployment, kubernetesMap)));
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

    private void run(String sessionId, String username, ApplicationKubernetesParam.DeploymentRequest deployment,
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
                     // 异步处理
                    ((KubernetesWebShExecChannelHandler) AopContext.currentProxy()).sendUserLoginContainerNotice(
                            username, deployment, pod);
                    // 记录
                    simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
                    kubernetesRemoteInvokeHandler.invokeExecWatch(sessionId, instanceId, kubernetes, pod, auditPath);
                });
    }

    @Async
    public void sendUserLoginContainerNotice(String username, ApplicationKubernetesParam.DeploymentRequest deployment,
                                             ApplicationKubernetesParam.PodRequest pod) {
        try {
            EnvParam.QueryEnvByGroupValue queryEnvByGroupValue = EnvParam.QueryEnvByGroupValue.builder()
                    .groupValue("prod")
                    .build();
            List<EnvVO.Env> prodEnvs = envFacade.queryEnvByGroupValue(queryEnvByGroupValue);
            if (!CollectionUtils.isEmpty(prodEnvs) && prodEnvs.stream()
                    .anyMatch(env -> env.getEnvName()
                            .equals(pod.getNamespace()))) {
                DingtalkRobotModel.Msg msg = getMsg(userService.getByUsername(username), deployment, pod);
                sendUserLoginContainerNotice(msg);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
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

    /// ///////////////////

    protected DingtalkRobotModel.Msg getMsg(User loginUser, ApplicationKubernetesParam.DeploymentRequest deployment,
                                            ApplicationKubernetesParam.PodRequest pod) throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate();
        String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put("loginUser", UserDisplayUtils.getDisplayName(loginUser))
                .put("kubernetesClusterName", deployment.getKubernetesClusterName())
                .put("podName", pod.getName())
                .put("containerName", pod.getContainer()
                        .getName())
                .put("namespace", pod.getNamespace())
                .put("loginTime", TimeUtils.parse(new Date(), Global.ISO8601))
                .build());
        return DingtalkRobotModel.loadAs(msg);
    }

    @SuppressWarnings("unchecked")
    private void sendUserLoginContainerNotice(DingtalkRobotModel.Msg message) {
        List<EdsInstance> edsInstanceList = edsInstanceHelper.queryValidEdsInstance(EdsInstanceTypeEnum.DINGTALK_ROBOT,
                "InspectionNotification");
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send inspection notifications.");
            return;
        }
        List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>> holders = (List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>>) edsInstanceHelper.buildHolder(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        holders.forEach(providerHolder -> {
            EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                    .getEdsInstance()
                    .getConfigId());
            EdsDingtalkConfigModel.Robot robot = providerHolder.getProvider()
                    .produceConfig(edsConfig);
            dingtalkService.send(robot.getToken(), message);
            providerHolder.importAsset(message);
        });
    }

    private NotificationTemplate getNotificationTemplate() {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(KUBERNETES_WSH_EXEC_NOTICE.name())
                .lang(language)
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

}
