package com.baiyi.cratos.facade.kubernetes.details.handler.impl;

import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam.QueryKubernetesDetails;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDetailsFacade;
import com.baiyi.cratos.facade.kubernetes.details.handler.BaseKubernetesDetailsChannelHandler;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 17:19
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SubscriptionKubernetesDetailsChannelHandler extends BaseKubernetesDetailsChannelHandler {

    private final ApplicationKubernetesDetailsFacade deploymentFacade;

    @Override
    public String getTopic() {
        return HasTopic.APPLICATION_KUBERNETES_WORKLOAD;
    }

    @Override
    public void handleRequest(String sessionId, Session session,
                              ApplicationKubernetesParam.KubernetesDetailsRequest message) throws IllegalStateException {
        if (SocketActionRequestEnum.SUBSCRIPTION.name()
                .equalsIgnoreCase(message.getAction())) {
            QueryKubernetesDetails param = QueryKubernetesDetails.builder()
                    .applicationName(message.getApplicationName())
                    .namespace(message.getNamespace())
                    .build();
            MessageResponse<KubernetesVO.KubernetesDetails> response = deploymentFacade.queryKubernetesDetails(
                    param);
            send(session, response);
        }
    }

}
