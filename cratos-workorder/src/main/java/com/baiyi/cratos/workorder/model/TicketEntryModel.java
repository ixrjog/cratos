package com.baiyi.cratos.workorder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/31 11:27
 * &#064;Version 1.0
 */
public class TicketEntryModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntryDesc {
        private String name;
        private String namespaces;
        private String desc;
    }

}
