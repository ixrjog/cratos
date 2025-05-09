package com.baiyi.cratos.workorder.holder.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/9 17:22
 * &#064;Version 1.0
 */
public class ApplicationDeletePodToken {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token implements Serializable {
        @Serial
        private static final long serialVersionUID = 6665885925992588311L;

        public static final Token NO_TOKEN = Token.builder()
                .valid(false)
                .build();
        private String username;
        private String applicationName;
        private Integer ticketId;
        private Date expires;
        @Builder.Default
        private Boolean valid = true;
    }

}
