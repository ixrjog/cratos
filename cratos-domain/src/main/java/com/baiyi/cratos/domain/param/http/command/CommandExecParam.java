package com.baiyi.cratos.domain.param.http.command;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    public static class AddCommandExec implements IToTarget<CommandExec>, HasSessionUser {
        @Null
        private Integer id;
        private String username;
        @NotNull
        private Boolean autoExec;
        private String ccTo;
        @NotBlank
        private String applyRemark;
        @NotBlank
        private String command;
        private final Boolean completed = false;
        private final Boolean success = false;

        @Override
        public void setSessionUser(String username) {
            this.username = username;
        }
    }

}
