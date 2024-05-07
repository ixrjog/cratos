package com.baiyi.cratos.domain.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 上午11:14
 * &#064;Version 1.0
 */
public class TestMessage {

    @Data
    @Builder
    @Schema
    public static class Test implements IEventMessage {

        @Serial
        private static final long serialVersionUID = -7837186387164840596L;
        private String msg;

    }

}
