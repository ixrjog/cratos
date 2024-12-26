package com.baiyi.cratos.facade.kubernetes.node.handler.impl;

import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.param.http.eds.EdsKubernetesNodeParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesNodeDetailsFacade;
import com.baiyi.cratos.facade.kubernetes.node.handler.BaseEdsKubernetesNodeDetailsChannelHandler;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/26 14:06
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SubscriptionKubernetesNodeDetailsChannelHandler extends BaseEdsKubernetesNodeDetailsChannelHandler<com.baiyi.cratos.domain.param.socket.kubernetes.EdsKubernetesNodeParam.EdsKubernetesNodeDetailsRequest> {

    private final KubernetesNodeDetailsFacade kubernetesNodeDetailsFacade;

    @Override
    public String getTopic() {
        return HasTopic.EDS_KUBERNETES_NODE_DETAILS;
    }

    @Override
    public void handleRequest(String sessionId, Session session,
                              com.baiyi.cratos.domain.param.socket.kubernetes.EdsKubernetesNodeParam.EdsKubernetesNodeDetailsRequest message) throws IllegalStateException, IOException {
        if (SocketActionRequestEnum.SUBSCRIPTION.name()
                .equalsIgnoreCase(message.getAction())) {
            EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails param = EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails.builder()
                    .instanceName(message.getInstanceName())
                    .build();
            MessageResponse<KubernetesNodeVO.KubernetesNodeDetails> response = kubernetesNodeDetailsFacade.queryEdsKubernetesNodeDetails(
                    param);
            send(session, response);
        }
    }

}
