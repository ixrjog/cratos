package com.baiyi.cratos.domain.param.http.credential;

import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/15 10:38
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ApplicationCredentialParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ApplicationCredentialPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;

        public EdsInstanceParam.AssetPageQuery toAssetPageQuery() {
            return EdsInstanceParam.AssetPageQuery.builder()
                    .queryName(this.getQueryName())
                    .valid(true)
                    .page(this.getPage())
                    .length(this.getLength())
                    .build();
        }
    }

}
