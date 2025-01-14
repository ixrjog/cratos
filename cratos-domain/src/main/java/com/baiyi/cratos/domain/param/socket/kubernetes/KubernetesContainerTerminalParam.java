package com.baiyi.cratos.domain.param.socket.kubernetes;

import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/8 13:53
 * &#064;Version 1.0
 */
public class KubernetesContainerTerminalParam {

    public static KubernetesContainerTerminalRequest loadAs(String message) throws JsonSyntaxException {
        return new GsonBuilder().create()
                .fromJson(message, KubernetesContainerTerminalRequest.class);
    }

    @Data
    @Builder
    public static class KubernetesContainerTerminalRequest implements HasSocketRequest {

        public static final KubernetesContainerTerminalRequest CLOSE = KubernetesContainerTerminalRequest.builder()
                .topic(HasTopic.APPLICATION_KUBERNETES_POD_EXEC)
                .action(SocketActionRequestEnum.CLOSE.name())
                .build();

        private String topic;
        private String action;
        //@NotBlank
        private String applicationName;
        //@NotBlank
        private String namespace;
        private String name;
        // Watch container logs
        private List<ApplicationKubernetesParam.DeploymentRequest> deployments;

        public SimpleBusiness toBusiness(int applicationId) {
           return SimpleBusiness.builder()
                   .businessType(BusinessTypeEnum.APPLICATION.name())
                   .businessId(applicationId)
                   .build();
        }

    }

}
