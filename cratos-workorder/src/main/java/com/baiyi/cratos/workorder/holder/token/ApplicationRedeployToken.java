package com.baiyi.cratos.workorder.holder.token;

import com.baiyi.cratos.domain.constant.Global;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/15 11:09
 * &#064;Version 1.0
 */
public class ApplicationRedeployToken {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token implements Serializable {
        @Serial
        private static final long serialVersionUID = -6475529578396407509L;
        public static final Token NO_TOKEN = Token.builder()
                .valid(false)
                .build();
        private String username;
        private String applicationName;
        private Integer ticketId;
        private String ticketNo;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expires;
        @Builder.Default
        private Boolean valid = true;
        @Builder.Default
        private String desc = "Temporary authorization on workOrder";
    }

}
