package com.baiyi.cratos.facade.kubernetes.sh;

import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.channel.factory.KubernetesSshChannelHandlerFactory;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.kubernetes.details.KubernetesRemoteInvokeHandler;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/8 14:04
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseKubernetesWebShChannelHandler<T extends HasSocketRequest> implements BaseChannelHandler<T> {

    protected final SimpleSshSessionFacade simpleSshSessionFacade;
    protected final KubernetesRemoteInvokeHandler kubernetesRemoteInvokeHandler;
    protected final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    protected final EdsInstanceService edsInstanceService;
    protected final SshAuditProperties sshAuditProperties;

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

    @Override
    public void afterPropertiesSet() {
        KubernetesSshChannelHandlerFactory.register(this);
    }

}
