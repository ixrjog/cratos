package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.dds20151201.models.DescribeDBInstancesRequest;
import com.aliyun.dds20151201.models.DescribeDBInstancesResponse;
import com.aliyun.dds20151201.models.DescribeDBInstancesResponseBody;
import com.baiyi.cratos.eds.aliyun.client.AliyunDdsClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/2 上午10:58
 * &#064;Version 1.0
 */
public class AliyunDdsMongoRepo {

    public static List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> describeDBInstances(
            String endpoint, EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        DescribeDBInstancesRequest request = new DescribeDBInstancesRequest().setPageNumber(1)
                .setPageSize(100);
        com.aliyun.dds20151201.Client client = AliyunDdsClient.createClient(endpoint, aliyun);
        List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> instanceList = Lists.newArrayList();
        while (true) {
            DescribeDBInstancesResponse response = client.describeDBInstances(request);
            List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> results = Optional.of(
                            response)
                    .map(DescribeDBInstancesResponse::getBody)
                    .map(DescribeDBInstancesResponseBody::getDBInstances)
                    .map(DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstances::getDBInstance)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(results)) {
                return instanceList;
            } else {
                instanceList.addAll(results);
            }
            if (Optional.of(response)
                    .map(DescribeDBInstancesResponse::getBody)
                    .map(DescribeDBInstancesResponseBody::getTotalCount)
                    .orElse(0) <= instanceList.size()) {
                return instanceList;
            } else {
                request.setPageNumber(request.getPageNumber() + 1);
            }
        }
    }

}
