package com.baiyi.cratos.domain.param.http.eds;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/24 11:03
 * &#064;Version 1.0
 */
public class EdsAssetParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AssetIndexPageQueryParam extends PageParam {
        private Integer instanceId;
    }

}
