package com.baiyi.cratos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2025/12/11 18:08
 * @Version 1.0
 */
public class DNS {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceRecordSet implements Serializable {
        @Serial
        private static final long serialVersionUID = 8375304026942660282L;

        public static final ResourceRecordSet NO_DATA = ResourceRecordSet.builder()
                .isNoData(true)
                .build();

        private String name;
        private String type;
        private List<ResourceRecord> resourceRecords;

        @Builder.Default
        private boolean isNoData = false;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceRecord implements Serializable {
        @Serial
        private static final long serialVersionUID = 1534989500445989828L;
        private String value;
        private Long weight;
        private Long tTL;
        // proxied = false (Only DNS)
        private Boolean proxied;
    }

}
