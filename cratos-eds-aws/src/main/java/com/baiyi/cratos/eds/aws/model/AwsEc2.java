package com.baiyi.cratos.eds.aws.model;

import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午3:24
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsEc2 {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ec2 implements HasRegionId {
        private String regionId;
        private Instance instance;
        private InstanceModel.EC2InstanceType instanceType;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Vpc implements HasRegionId {
        private String regionId;
        private com.amazonaws.services.ec2.model.Vpc vpc;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Subnet implements HasRegionId {
        private String regionId;
        private com.amazonaws.services.ec2.model.Subnet subnet;
    }

}
