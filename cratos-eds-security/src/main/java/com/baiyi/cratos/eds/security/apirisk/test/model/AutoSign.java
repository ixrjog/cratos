package com.baiyi.cratos.eds.security.apirisk.test.model;

import com.baiyi.cratos.domain.YamlUtils;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/9 10:23
 * &#064;Version 1.0
 */
public class AutoSign {

    public static SignMap loadAs(String content) {
        if (StringUtils.isBlank(content)) {
            return SignMap.EMPTY;
        }
        try {
            return YamlUtils.loadAs(content, SignMap.class);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Auto sign config format error:");
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignMap implements Serializable {
        @Serial
        private static final long serialVersionUID = -945971912916542487L;

        public static final SignMap EMPTY = SignMap.builder()
                .build();

        @Builder.Default
        private Map<String, List<String>> content = Map.of();
    }

}
