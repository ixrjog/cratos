package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.dds20151201.models.DescribeDBInstancesRequest;
import com.aliyun.dds20151201.models.DescribeDBInstancesResponse;
import com.aliyun.dds20151201.models.DescribeDBInstancesResponseBody;
import com.baiyi.cratos.eds.aliyun.client.AliyunDdsClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/2 上午10:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunDdsMongoRepo {

    public static List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> describeDBInstances(
            String endpoint, EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        DescribeDBInstancesRequest request = new DescribeDBInstancesRequest().setPageNumber(1)
                .setPageSize(100);
        com.aliyun.dds20151201.Client client = AliyunDdsClient.createClient(endpoint, aliyun);
        List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> instanceList = Lists.newArrayList();

        int totalCount = Integer.MAX_VALUE;
        while (instanceList.size() < totalCount) {
            DescribeDBInstancesResponse response = client.describeDBInstances(request);
            DescribeDBInstancesResponseBody body = response.getBody();
            if (body == null || body.getDBInstances() == null || CollectionUtils.isEmpty(body.getDBInstances()
                    .getDBInstance())) {
                break;
            }
            instanceList.addAll(body.getDBInstances()
                    .getDBInstance());
            totalCount = Optional.ofNullable(body.getTotalCount())
                    .orElse(0);
            request.setPageNumber(request.getPageNumber() + 1);
        }
        return instanceList;
    }

}
