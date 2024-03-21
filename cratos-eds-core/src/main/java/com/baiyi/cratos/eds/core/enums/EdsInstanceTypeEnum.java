package com.baiyi.cratos.eds.core.enums;

import com.baiyi.cratos.domain.view.base.OptionsVO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/2/26 15:09
 * @Version 1.0
 */
public enum EdsInstanceTypeEnum {

    ALIYUN,
    AWS,
    CLOUDFLARE,
    KUBERNETES,
    LDAP,
    GITLAB;

    public static OptionsVO.Options toOptions(){
        List<OptionsVO.Option> optionList = Arrays.stream(EdsInstanceTypeEnum.values()).map(e -> OptionsVO.Option.builder()
                .label(e.name())
                .value(e.name())
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

}