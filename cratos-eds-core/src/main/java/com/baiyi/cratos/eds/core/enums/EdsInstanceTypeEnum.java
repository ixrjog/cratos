package com.baiyi.cratos.eds.core.enums;

import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.eds.core.annotation.Acme;
import com.baiyi.cratos.eds.core.annotation.DataCenter;

import java.lang.annotation.Annotation;
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
    @Acme @DataCenter ALIYUN,
    @DataCenter
    UCLOUD,
    @DataCenter
    AWS,
    @DataCenter
    HUAWEICLOUD,
    @DataCenter
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
    CRT,
    SRE_EVENTBRIDGE,
    @DataCenter
    CUSTOM_IDC,
    APIRISK;

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

    public static final List<EdsInstanceTypeEnum> ACME_TYPES = getTypes(Acme.class);

    public static final List<EdsInstanceTypeEnum> DATACENTER_TYPES = getTypes(DataCenter.class);

    private static List<EdsInstanceTypeEnum> getTypes(Class<? extends Annotation> annotationClass) {
        return Arrays.stream(EdsInstanceTypeEnum.values())
                .filter(assetType -> {
                    try {
                        Field field = EdsInstanceTypeEnum.class.getField(assetType.name());
                        return field.isAnnotationPresent(annotationClass);
                    } catch (NoSuchFieldException e) {
                        return false;
                    }
                })
                .toList();
    }

}