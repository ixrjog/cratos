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
 * &#064;Date  2024/5/27 下午2:06
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshCommandParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class SshCommandPageQuery extends PageParam {

        private Integer sshSessionInstanceId;

        private String inputFormatted;

    }

}
