package com.baiyi.cratos.domain.param.http.command;

import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 15:52
 * &#064;Version 1.0
 */
public class CommandExecParam {

    @Data
    @Schema
    public static class AddCommandExec implements IToTarget<CommandExec> {
        @Null
        private Integer id;
        private String username;
        private Boolean autoExec;
        private String ccTo;
        private String applyRemark;
        private String command;
    }

}
