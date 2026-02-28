package com.baiyi.cratos.eds.core.enums;

import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.eds.core.annotation.Acme;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/2/26 15:09
 * &#064;Version  1.0
 */
public enum EdsInstanceTypeEnum {

    CRATOS,
    @Acme ALIYUN,
    AWS,
    HUAWEICLOUD,
    HUAWEICLOUD_STACK,
    CLOUDFLARE,
    KUBERNETES,
    LDAP,
    GITLAB,
    DINGTALK_APP,
    DINGTALK_ROBOT,
    GANDI,
    GODADDY,
    // https://goharbor.io/
    HARBOR,
    GCP,
    AZURE,
    OPSCLOUD,
    ALIMAIL,
    JENKINS,
    EAGLECLOUD_SASE,
    ZABBIX,
    CRT;

    public static OptionsVO.Options toOptions() {
        List<OptionsVO.Option> optionList = Arrays.stream(EdsInstanceTypeEnum.values())
                .map(e -> OptionsVO.Option.builder()
                        .label(e.name())
                        .value(e.name())
                        .build())
                .collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static final List<EdsInstanceTypeEnum> ACME_TYPES = getAcmeTypes();

    private static List<EdsInstanceTypeEnum> getAcmeTypes() {
        return Arrays.stream(EdsInstanceTypeEnum.values())
                .filter(assetType -> {
                    try {
                        Field field = EdsInstanceTypeEnum.class.getField(assetType.name());
                        return field.isAnnotationPresent(Acme.class);
                    } catch (NoSuchFieldException e) {
                        return false;
                    }
                })
                .toList();
    }

}