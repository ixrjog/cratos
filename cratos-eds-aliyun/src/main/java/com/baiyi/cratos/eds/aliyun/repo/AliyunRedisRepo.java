package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest;
import com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse;
import com.baiyi.cratos.eds.aliyun.client.common.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/5 上午10:46
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRedisRepo {

    private final AliyunClient aliyunClient;

    public static final String QUERY_ALL_INSTANCE = null;
    private static final int PAGE_SIZE = 50;

    public List<DescribeInstancesResponse.KVStoreInstance> listInstance(String regionId, EdsAliyunConfigModel.Aliyun aliyun) throws ClientException {
        return listInstance(regionId, aliyun, QUERY_ALL_INSTANCE);
    }

    public List<DescribeInstancesResponse.KVStoreInstance> listInstance(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceIds) throws ClientException {
        List<DescribeInstancesResponse.KVStoreInstance> instances = Lists.newArrayList();
        DescribeInstancesRequest request = new  DescribeInstancesRequest();
        if (!StringUtils.isEmpty(instanceIds)) {
            request.setInstanceIds(instanceIds);
        }
        request.setPageSize(PAGE_SIZE);
        int size = PAGE_SIZE;
        int pageNumber = 1;
        while (PAGE_SIZE <= size) {
            request.setPageNumber(pageNumber);
            DescribeInstancesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            instances.addAll(response.getInstances());
            size = response.getTotalCount();
            pageNumber++;
        }
        return instances;
    }

}
