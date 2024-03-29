package com.baiyi.cratos.domain.view.traffic;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.IResourceCount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:32
 * @Version 1.0
 */
public class TrafficLayerDomainVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Domain extends BaseVO implements IResourceCount, Serializable {

        @Serial
        private static final long serialVersionUID = -8990726488134957647L;

        private Integer id;

        private String name;

        private String domain;

        private Boolean valid;

        private String comment;

        private Map<String, Integer> resourceCount;

    }

}
