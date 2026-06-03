package com.baiyi.cratos.eds.aliyun.model;

import com.aliyun.slb20140515.models.DescribeLoadBalancerAttributeResponseBody;
import com.aliyun.slb20140515.models.DescribeLoadBalancersResponseBody;
import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/2 16:54
 * &#064;Version 1.0
 */
public class AliyunClb {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Clb implements HasRegionId {
        private String endpoint;
        private String regionId;
        private DescribeLoadBalancersResponseBody.DescribeLoadBalancersResponseBodyLoadBalancersLoadBalancer loadBalancer;
        private DescribeLoadBalancerAttributeResponseBody attribute;
    }

}
