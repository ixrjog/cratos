package com.baiyi.cratos.domain.model.cratos;


import com.baiyi.cratos.domain.model.cratos.builder.AssetFieldsBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public static class ComputerFieldMapper implements CratosCommonModel.HasFieldMapper {

        public static final ComputerFieldMapper DATA = ComputerFieldMapper.builder()
                .build();

        @Builder.Default
        private Map<String, CratosCommonModel.AssetFieldDesc> fields = AssetFieldsBuilder.newBuilder()
                .withField("assetId", "Computer ID", false)
                .withField("name", "Computer Name")
                .withField("assetKey", "Remote Management IP")
                .get();
    }

}
