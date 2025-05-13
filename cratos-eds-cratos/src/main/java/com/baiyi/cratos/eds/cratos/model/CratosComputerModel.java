package com.baiyi.cratos.eds.cratos.model;

import com.baiyi.cratos.eds.core.annotation.AssetField;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/9 13:52
 * &#064;Version 1.0
 */
public class CratosComputerModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Computer implements Serializable {
        @Serial
        private static final long serialVersionUID = -7555238264994084804L;
        private Integer parentId;
        private Integer instanceId;
        /**
         * 资产名称
         */
        @AssetField(aliasName = "Computer Name")
        private String name;

        private String assetId;
        @AssetField(aliasName = "Remote Management IP")
        private String assetKey;
        private final String assetType = EdsAssetTypeEnum.CRATOS_COMPUTER.name();
        private String kind;
        private String version;
        private Boolean valid;
        @AssetField(aliasName = "Region", required = false)
        private String region;
        @AssetField(aliasName = "Zone", required = false)
        private String zone;
        private String assetStatus;
        private String originalModel;
        @AssetField(aliasName = "Description", required = false)
        private String description;
        private List<String> tags;

        private ConfigMap configMap;
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

}
