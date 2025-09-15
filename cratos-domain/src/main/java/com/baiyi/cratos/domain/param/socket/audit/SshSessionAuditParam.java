package com.baiyi.cratos.domain.param.socket.audit;

import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/25 15:53
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshSessionAuditParam {

    public static AuditRequest loadAs(String message) throws JsonSyntaxException {
        return new GsonBuilder().create()
                .fromJson(message, AuditRequest.class);
    }

    @Data
    @Builder
    @Schema
    public static class AuditRequest implements HasSocketRequest {
        private String topic;
        private String action;

        private String sessionId;
        private String instanceId;
    }

}
