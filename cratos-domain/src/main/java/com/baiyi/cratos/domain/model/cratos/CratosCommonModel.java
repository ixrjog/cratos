package com.baiyi.cratos.domain.model.cratos;

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
 * &#064;Date  2025/5/13 10:55
 * &#064;Version 1.0
 */
public class CratosCommonModel {

    public interface HasFieldMapper {
        Map<String, AssetFieldDesc> getFields();

        void setFields(Map<String, AssetFieldDesc> fields);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetFieldDesc implements Serializable {
        @Serial
        private static final long serialVersionUID = -1054269947639389764L;

        private String name;
        private String desc;
        private Boolean required;

    }

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

}
