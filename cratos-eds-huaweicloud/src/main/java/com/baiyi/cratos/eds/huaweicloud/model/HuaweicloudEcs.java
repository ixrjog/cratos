package com.baiyi.cratos.eds.huaweicloud.model;

import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午11:09
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HuaweicloudEcs {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ecs {

        private String regionId;

        private ServerDetail serverDetail;

    }

}
