package com.baiyi.cratos.eds.aliyun.model;

import com.aliyun.alb20200616.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/10 09:57
 * &#064;Version 1.0
 */
public class AliyunAlb {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Alb implements HasRegionId {
        private String regionId;
        private ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers loadBalancers;
    }

}
