package com.baiyi.cratos.common.enums;

import com.baiyi.cratos.domain.view.base.OptionsVO;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/15 10:24
 * &#064;Version 1.0
 */
@Getter
public enum TrafficRecordTargetTypes {

    CLOUDFLARE("CloudFlare"),
    ALIYUN_ALB("Aliyun ALB"),
    AWS_ELB("AWS ELB"),
    AWS_CLOUDFRONT("CloudFront"),
    HWC_ELB("HUAWEI CLOUD ELB"),
    IP("IP");

    private final String desc;

    TrafficRecordTargetTypes(String desc) {
        this.desc = desc;
    }

    public static OptionsVO.Options toOptions(){
        List<OptionsVO.Option> optionList = Arrays.stream(TrafficRecordTargetTypes .values()).map(e -> OptionsVO.Option.builder()
                .label(e.desc)
                .value(e.name())
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

}
