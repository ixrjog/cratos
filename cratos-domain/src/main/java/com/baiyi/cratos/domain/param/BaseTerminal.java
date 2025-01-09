package com.baiyi.cratos.domain.param;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/9 10:39
 * &#064;Version 1.0
 */
public class BaseTerminal {

    @Data
    @NoArgsConstructor
    public static class Terminal {
        private Integer width;
        private Integer height;

        private Integer cols;
        private Integer rows;
    }

}
