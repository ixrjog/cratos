package com.baiyi.cratos.eds.aliyun.model;

import com.aliyun.nlb20220430.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/4 15:33
 * &#064;Version 1.0
 */
public class AliyunNlb {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Nlb implements HasRegionId {
        private String regionId;
        private ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers loadBalancers;
    }

}
