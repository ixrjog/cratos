package com.baiyi.cratos.domain.param.http.eds.cratos;

import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/19 17:45
 * &#064;Version 1.0
 */
public class CustomAssetParam {

    public interface HasTags {
        List<String> getTags();

        void setTags(List<String> tags);
    }

    public interface HasConfigMap {
        ConfigMap getConfigMap();

        void setConfigMap(ConfigMap configMap);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigMap implements Serializable {
        @Serial
        private static final long serialVersionUID = 9030170925342229200L;
        private Map<String, String> data;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddAsset extends AssetPrototype implements HasTags, HasConfigMap, IToTarget<AssetPrototype> {
        private List<String> tags;
        private ConfigMap configMap;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateAsset extends AssetPrototype implements HasTags, HasConfigMap, IToTarget<AssetPrototype> {
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
        private List<String> tags;
        private ConfigMap configMap;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "资产原型")
    public static class AssetPrototype {
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
    }

}
