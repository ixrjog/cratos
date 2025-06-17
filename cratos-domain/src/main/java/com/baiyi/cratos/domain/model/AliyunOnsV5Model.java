package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author 修远
 * @Date 2025/6/11 18:03
 * @Since 1.0
 */
public class AliyunOnsV5Model {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Topic implements Serializable {

        @Serial
        private static final long serialVersionUID = -7405060456827694144L;
        private EdsInstanceVO.EdsInstance edsInstance;
        private String regionId;
        private String onsInstanceName;
        private String onsInstanceId;
        private String topicName;
        private String messageType;
        private String remark;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ConsumerGroup implements Serializable {

        @Serial
        private static final long serialVersionUID = 5456910395906968504L;
        private EdsInstanceVO.EdsInstance edsInstance;
        private String regionId;
        private String onsInstanceName;
        private String onsInstanceId;
        private String consumerGroupId;
        private String deliveryOrderType;
        private String remark;
        private ConsumeRetryPolicy consumeRetryPolicy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsumeRetryPolicy implements Serializable {

        @Serial
        private static final long serialVersionUID = -2721913561803187459L;
        private String deadLetterTargetTopic;
        private Integer maxRetryTimes;
        private String retryPolicy;
    }

}
