package com.baiyi.cratos.ssh.kubernetes.handler.base;

import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.channel.factory.KubernetesSshChannelHandlerFactory;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.baiyi.cratos.domain.param.socket.kubernetes.KubernetesContainerTerminalParam;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.facade.AccessControlFacade;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.kubernetes.invoker.KubernetesRemoteInvoker;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/8 14:04
 * &#064;Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseKubernetesWebShChannelHandler<T extends HasSocketRequest> implements BaseChannelHandler<T> {

    protected final SimpleSshSessionFacade simpleSshSessionFacade;
    protected final KubernetesRemoteInvoker kubernetesRemoteInvokeHandler;
    protected final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    protected final EdsInstanceService edsInstanceService;
    protected final SshAuditProperties sshAuditProperties;
    private final AccessControlFacade accessControlFacade;
    private final ApplicationService applicationService;

    @SuppressWarnings("unchecked")
    protected EdsKubernetesConfigModel.Kubernetes getKubernetes(
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

    protected boolean accessInterception(String username,
                                         KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest message) {
        try {
            Application application = applicationService.getByName(message.getApplicationName());
            if (application == null) {
                return false;
            }
            message.toBusiness(application.getId());
            AccessControlVO.AccessControl accessControl = accessControlFacade.generateAccessControl(username,
                    message.toBusiness(application.getId()), message.getNamespace());
            return accessControl.getPermission();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() {
        KubernetesSshChannelHandlerFactory.register(this);
    }

}
