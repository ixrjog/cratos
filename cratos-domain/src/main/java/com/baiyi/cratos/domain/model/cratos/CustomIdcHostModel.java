package com.baiyi.cratos.domain.model.cratos;

import com.baiyi.cratos.domain.model.cratos.builder.AssetFieldsBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/19 17:29
 * &#064;Version 1.0
 */
public class CustomIdcHostModel {

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HostFieldMapper implements HasFieldMapper {

        public static final HostFieldMapper DATA = HostFieldMapper.builder()
                .build();

        @Builder.Default
        private Map<String, AssetFieldDesc> fields = AssetFieldsBuilder.newBuilder()
                .withField("assetId", "Host ID", false)
                .withField("name", "Host Name")
                .withField("assetKey", "Remote Management IP")
                .get();
    }

}
