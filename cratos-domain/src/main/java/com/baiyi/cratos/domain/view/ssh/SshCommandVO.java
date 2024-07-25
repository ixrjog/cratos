package com.baiyi.cratos.domain.view.ssh;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 下午1:53
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshCommandVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.SSH_COMMAND)
    public static class Command extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 6289635292393271645L;
        private Integer id;
        private Integer sshSessionInstanceId;
        private String prompt;
        private Boolean isFormatted;
        private String input;
        private String inputFormatted;
        private String output;
    }

}
