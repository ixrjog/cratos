package com.baiyi.cratos.eds.aliyun.model;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/19 下午3:26
 * &#064;Version 1.0
 */
public class AliyunVirtualSwitch {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Switch {
        private String regionId;
        private DescribeVSwitchesResponse.VSwitch virtualSwitch;
    }

}
