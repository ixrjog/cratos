package com.baiyi.cratos.eds.alimail.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/18 10:30
 * &#064;Version 1.0
 */
public class AlimailUserParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UpdateUser implements Serializable {
        public static final UpdateUser FREEZE_USER = UpdateUser.builder()
                .status("FREEZE")
                .build();
        @Serial
        private static final long serialVersionUID = 4160100080314040653L;

        private String status;
    }

}
