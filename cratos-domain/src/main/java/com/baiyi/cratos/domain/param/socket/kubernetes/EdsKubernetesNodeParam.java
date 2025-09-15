package com.baiyi.cratos.domain.param.socket.kubernetes;

import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/26 14:13
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsKubernetesNodeParam {

    public static EdsKubernetesNodeDetailsRequest loadAs(String message) throws JsonSyntaxException {
        return new GsonBuilder().create()
                .fromJson(message, EdsKubernetesNodeDetailsRequest.class);
    }

    @Data
    @Builder
    public static class EdsKubernetesNodeDetailsRequest implements HasSocketRequest {
        private String topic;
        private String action;
        private String instanceName;
    }

}
