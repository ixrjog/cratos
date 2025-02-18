package com.baiyi.cratos.domain.view.command;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.env.EnvVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/18 14:45
 * &#064;Version 1.0
 */
public class CommandExecVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class CommandExec extends BaseVO implements EnvVO.HasEnv, Serializable {
        @Serial
        private static final long serialVersionUID = -7247001798442829593L;
        private Integer id;
        private String username;
        private Boolean autoExec;
        private String approvedBy;
        private String ccTo;
        private Boolean completed;
        private Date completedAt;
        private String namespace;
        private Boolean success;
        private String applyRemark;
        private String command;
        private String execTargetContent;
        private String outMsg;
        private String errorMsg;
        private EnvVO.Env env;
        @Override
        public String getEnvName() {
            return this.namespace;
        }
    }

}
