package com.baiyi.cratos.domain.param.http.kms;

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
 * &#064;Date  2025/6/23 10:08
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunKmsParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class SecretPageQuery extends PageParam {
        private Integer instanceId;
        @Schema(description = "查询名称")
        private String queryName;
        private String createdBy;

        public EdsInstanceParam.AssetPageQuery toAssetPageQuery() {
            return EdsInstanceParam.AssetPageQuery.builder()
                    .queryName(this.getQueryName())
                    .instanceId(this.getInstanceId())
                    .valid(true)
                    .page(this.getPage())
                    .length(this.getLength())
                    .build();
        }
    }

}
