package com.baiyi.cratos.domain.param.ssh;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午11:13
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshSessionParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class SshSessionPageQuery extends PageParam {

        @Schema(description = "查询用户")
        private String username;

        private String sessionStatus;

        private String sessionType;

    }

}
