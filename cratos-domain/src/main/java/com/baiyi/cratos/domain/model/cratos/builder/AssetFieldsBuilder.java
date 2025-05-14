package com.baiyi.cratos.domain.model.cratos.builder;

import com.baiyi.cratos.domain.model.cratos.CratosCommonModel;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/14 09:35
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssetFieldsBuilder {

    private final Map<String, CratosCommonModel.AssetFieldDesc> fields = Maps.newHashMap();

    public static AssetFieldsBuilder newBuilder() {
        return new AssetFieldsBuilder();
    }

    public AssetFieldsBuilder withField(String fieldName, String desc, Boolean required) {
        fields.put(fieldName, CratosCommonModel.AssetFieldDesc.builder()
                .name(fieldName)
                .desc(desc)
                .required(required)
                .build());
        return this;
    }

    public AssetFieldsBuilder withField(String fieldName, String desc) {
        fields.put(fieldName, CratosCommonModel.AssetFieldDesc.builder()
                .name(fieldName)
                .desc(desc)
                .required(true)
                .build());
        return this;
    }

    public Map<String, CratosCommonModel.AssetFieldDesc> get() {
        return fields;
    }

}
