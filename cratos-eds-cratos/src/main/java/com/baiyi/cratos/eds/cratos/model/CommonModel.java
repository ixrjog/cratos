package com.baiyi.cratos.eds.cratos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/13 10:55
 * &#064;Version 1.0
 */
public class CommonModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag implements Serializable {
        @Serial
        private static final long serialVersionUID = -4780191684126538053L;
        // key or key:value
        private String tag;
    }

}
