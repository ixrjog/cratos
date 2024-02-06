package com.baiyi.cratos.domain.param.eds;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:49
 * @Version 1.0
 */
public class EdsConfigParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class EdsConfigPageQuery extends PageParam {

        private String queryName;

        @Schema(description = "EDS Type")
        private String edsType;

        private Boolean valid;

    }

}
