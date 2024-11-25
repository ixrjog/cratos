package com.baiyi.cratos.domain.param.socket;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 14:31
 * &#064;Version 1.0
 */

public class BaseSocketRequest {

    public static SimpleRequest loadAs(String message) throws JsonSyntaxException {
        return new GsonBuilder().create()
                .fromJson(message, SimpleRequest.class);
    }

    @Data
    @Builder
    @Schema
    public static class SimpleRequest implements HasSocketRequest {
        private String topic;
        private String action;
    }

}
