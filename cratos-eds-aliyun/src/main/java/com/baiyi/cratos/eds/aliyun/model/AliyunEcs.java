package com.baiyi.cratos.eds.aliyun.model;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/11 上午11:02
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunEcs {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ecs {

        private String regionId;

        private DescribeInstancesResponse.Instance instance;

        private List<DescribeDisksResponse.Disk> disks;

    }

}
