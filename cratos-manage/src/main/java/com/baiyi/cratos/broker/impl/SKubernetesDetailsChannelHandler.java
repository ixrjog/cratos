package com.baiyi.cratos.broker.impl;

import com.baiyi.cratos.broker.HasChannelHandler;
import com.baiyi.cratos.common.MessageResponse;
import com.baiyi.cratos.domain.HasTopic;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesRequest;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDeploymentFacade;
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
public class SKubernetesDetailsChannelHandler implements HasChannelHandler<ApplicationKubernetesRequest.KubernetesDetailsRequest> {

    private final ApplicationKubernetesDeploymentFacade deploymentFacade;

    @Override
    public String getTopic() {
        return HasTopic.APPLICATION_KUBERNETES_DETAILS;
    }

    @Override
    public void handleRequest(String sessionId, Session session,
                              ApplicationKubernetesRequest.KubernetesDetailsRequest message) throws IOException {
        if (SocketActionRequestEnum.SUBSCRIPTION.name()
                .equalsIgnoreCase(message.getAction())) {
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails queryApplicationResourceKubernetesDetails = ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails.builder()
                    .applicationName(message.getApplicationName())
                    .namespace(message.getNamespace())
                    .build();

            MessageResponse<KubernetesVO.KubernetesDetails> response = deploymentFacade.queryKubernetesDeploymentDetails(
                    queryApplicationResourceKubernetesDetails);
            send(session, response);
        }
    }

}
