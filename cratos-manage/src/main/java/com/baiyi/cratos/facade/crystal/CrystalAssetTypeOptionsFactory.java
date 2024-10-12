package com.baiyi.cratos.facade.crystal;

import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.google.api.client.util.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/9 15:12
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class CrystalAssetTypeOptionsFactory {

    private static final Map<String, List<EdsAssetTypeEnum>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(EdsInstanceTypeEnum instanceTypeEnum, EdsAssetTypeEnum assetTypeEnum) {
        if (CONTEXT.containsKey(instanceTypeEnum.name())) {
            CONTEXT.get(instanceTypeEnum.name())
                    .add(assetTypeEnum);
        } else {
            List<EdsAssetTypeEnum> assetTypeEnumList = Lists.newArrayList();
            assetTypeEnumList.add(assetTypeEnum);
            CONTEXT.put(instanceTypeEnum.name(), assetTypeEnumList);
        }
    }

    public static OptionsVO.Options getOptions(String instanceType) {
        if (!CONTEXT.containsKey(instanceType)) {
            return OptionsVO.Options.builder()
                    .build();
        }
        List<EdsAssetTypeEnum> edsAssetTypeEnums = CONTEXT.get(instanceType);
        return OptionsVO.Options.builder()
                .options(edsAssetTypeEnums.stream()
                        .map(e -> OptionsVO.Option.builder()
                                .label(e.getDisplayName())
                                .value(e.name())
                                .build())
                        .toList())
                .build();
    }

}
