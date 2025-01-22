package com.baiyi.cratos.facade.kubernetes.details.handler.impl;

import com.baiyi.cratos.domain.annotation.TopicName;
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

import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 17:19
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@TopicName(nameOf = HasTopic.APPLICATION_KUBERNETES_DETAILS)
public class SubscriptionKubernetesDetailsChannelHandler extends BaseKubernetesDetailsChannelHandler<ApplicationKubernetesParam.KubernetesDetailsRequest> {

    private final ApplicationKubernetesDetailsFacade kubernetesDetailsFacade;

    @Override
    public void handleRequest(String sessionId, Session session,
                              ApplicationKubernetesParam.KubernetesDetailsRequest message) throws IllegalStateException, IOException {
        if (SocketActionRequestEnum.SUBSCRIPTION.name()
                .equalsIgnoreCase(message.getAction())) {
            QueryKubernetesDetails param = QueryKubernetesDetails.builder()
                    .applicationName(message.getApplicationName())
                    .namespace(message.getNamespace())
                    .name(message.getName())
                    .build();
            MessageResponse<KubernetesVO.KubernetesDetails> response = kubernetesDetailsFacade.queryKubernetesDetails(
                    param);
            send(session, response);
        }
    }

}
