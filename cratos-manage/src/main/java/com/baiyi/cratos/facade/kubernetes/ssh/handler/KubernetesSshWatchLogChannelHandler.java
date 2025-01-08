package com.baiyi.cratos.facade.kubernetes.ssh.handler;

import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.param.socket.kubernetes.KubernetesContainerTerminalParam;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.kubernetes.details.KubernetesRemoteInvokeHandler;
import com.baiyi.cratos.facade.kubernetes.ssh.BaseKubernetesSshChannelHandler;
import com.baiyi.cratos.service.EdsInstanceService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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


    @Override
    public String getTopic() {
        return HasTopic.APPLICATION_KUBERNETES_WATCH_LOG;
    }

    @Override
    public void handleRequest(String sessionId, Session session,
                              KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest message) throws IllegalStateException, IOException {
        if (SocketActionRequestEnum.SUBSCRIPTION.name()
                .equalsIgnoreCase(message.getAction())) {
            Map<Integer, EdsKubernetesConfigModel.Kubernetes> kubernetesMap = Maps.newHashMap();
            message.getDeployments()
                    .forEach(deployment -> {
                        EdsInstance edsInstance = edsInstanceService.getByName(deployment.getKubernetesClusterName());
                        if (edsInstance != null) {
                            EdsKubernetesConfigModel.Kubernetes kubernetes = getKubernetes(kubernetesMap,
                                    edsInstance.getId());
                            deployment.getPods()
                                    .forEach(pod -> kubernetesRemoteInvokeHandler.runWatchLog(sessionId,
                                            toInstanceId(pod), kubernetes, pod, 100));
                        }
                    });
        }
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
