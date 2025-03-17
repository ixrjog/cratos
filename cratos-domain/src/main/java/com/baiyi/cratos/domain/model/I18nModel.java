package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.YamlDump;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 11:21
 * &#064;Version 1.0
 */
public class I18nModel {

    public interface HasI18n {
        String getI18n();

        void setI18nData(I18nData data);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class I18nData extends YamlDump implements Serializable {
        @Serial
        private static final long serialVersionUID = -950956814213648037L;
        public static final I18nData NO_DATA = I18nData.builder()
                .build();
        @Builder.Default
        private Map<String, Alias> langMap = Map.of();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Alias {
        private String displayName;
        private String desc;
    }

}
