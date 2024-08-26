package com.baiyi.cratos.eds.business.wrapper.impl.network.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 11:34
 * &#064;Version 1.0
 */
public class MainModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Main {

        private String name;
        private String type;
        private Integer id;

    }

}
