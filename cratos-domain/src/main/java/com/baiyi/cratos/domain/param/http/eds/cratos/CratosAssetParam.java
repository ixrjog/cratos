package com.baiyi.cratos.domain.param.http.eds.cratos;

import com.baiyi.cratos.domain.model.cratos.CratosCommonModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/13 17:34
 * &#064;Version 1.0
 */
public class CratosAssetParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddCratosAsset implements CratosCommonModel.HasTags, CratosCommonModel.HasConfigMap {
        @Null
        private Integer id;
        private Integer parentId;
        @NotNull
        private Integer instanceId;
        @NotNull
        private String name;
        @NotNull
        private String assetId;
        @NotNull
        private String assetKey;
        @NotNull
        private String assetType;
        private String kind;
        private String version;
        private Boolean valid;
        private String region;
        private String zone;
        private String assetStatus;
        private String originalModel;
        private String description;
        private List<String> tags;
        private CratosCommonModel.ConfigMap configMap;
    }

}
