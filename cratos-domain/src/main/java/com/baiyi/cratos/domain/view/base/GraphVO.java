package com.baiyi.cratos.domain.view.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/23 下午6:00
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class GraphVO {

    @Data
    @NoArgsConstructor
    @Schema
    public static class SimpleData implements Serializable {
        @Serial
        private static final long serialVersionUID = -7198696996237859795L;
        @Schema(name = "别名")
        private String cName;
        private Integer value;
    }

}
