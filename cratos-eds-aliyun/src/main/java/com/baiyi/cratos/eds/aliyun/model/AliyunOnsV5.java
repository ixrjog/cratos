package com.baiyi.cratos.eds.aliyun.model;

import com.aliyun.rocketmq20220801.models.ListConsumerGroupSubscriptionsResponseBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/3 下午2:49
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunOnsV5 {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConsumerGroupSubscription {

        private String endpoint;

        private String instanceId;

        private ListConsumerGroupSubscriptionsResponseBody.ListConsumerGroupSubscriptionsResponseBodyData consumerGroupSubscription;

    }

}
