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

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/13 17:34
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CratosAssetParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddCratosAssetX implements CratosCommonModel.HasTags, CratosCommonModel.HasConfigMap {
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateCratosAssetX implements CratosCommonModel.HasTags, CratosCommonModel.HasConfigMap {
        @NotNull
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CratosAssetX implements CratosCommonModel.HasTags, CratosCommonModel.HasConfigMap {
        private Integer id;
        private Integer parentId;
        private Integer instanceId;
        private String name;
        private String assetId;
        private String assetKey;
        private String assetType;
        private String kind;
        private String version;
        private Boolean valid;
        private String region;
        private String zone;
        private String assetStatus;
        private String description;
        private List<String> tags;
        private CratosCommonModel.ConfigMap configMap;
    }

}
