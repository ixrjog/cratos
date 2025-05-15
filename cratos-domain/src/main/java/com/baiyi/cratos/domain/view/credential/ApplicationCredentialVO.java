package com.baiyi.cratos.domain.view.credential;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/15 10:21
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ApplicationCredentialVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Credential implements Serializable {
        @Serial
        private static final long serialVersionUID = -5559803352441937304L;
        private EdsAssetVO.Asset asset;
        @Builder.Default
        private List<EdsAssetIndex> indices = Lists.newArrayList();
    }

}
