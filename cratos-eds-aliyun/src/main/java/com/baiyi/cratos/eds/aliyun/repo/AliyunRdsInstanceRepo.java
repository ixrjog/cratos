package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.baiyi.cratos.eds.aliyun.client.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/5 下午3:22
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRdsInstanceRepo {

    private final AliyunClient aliyunClient;

    public static final String QUERY_ALL_INSTANCE = null;
    public static final int PAGE_SIZE = 50;
    public static final int RDS_INSTANCE_PAGE_SIZE = 30;

    /**
     * 查询所有数据库实例
     *
     * @param regionId
     * @param aliyun
     * @return
     */
    public List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> listDbInstance(String regionId,
                                                                                        EdsAliyunConfigModel.Aliyun aliyun) throws ClientException {
        return listDbInstance(regionId, aliyun, QUERY_ALL_INSTANCE);
    }

    /**
     * 查询数据库实例
     *
     * @param regionId
     * @param aliyun
     * @param dbInstanceId
     * @return
     */
    public List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> listDbInstance(String regionId,
                                                                                        EdsAliyunConfigModel.Aliyun aliyun,
                                                                                        String dbInstanceId) throws ClientException {
        List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> instances = Lists.newArrayList();
        DescribeDBInstancesRequest describe = new DescribeDBInstancesRequest();
        if (StringUtils.hasText(dbInstanceId)) {
            describe.setDBInstanceId(dbInstanceId);
        }
        describe.setPageSize(RDS_INSTANCE_PAGE_SIZE);
        int pageNumber = 1;
        while (true) {
            describe.setPageNumber(pageNumber);
            DescribeDBInstancesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
            if (CollectionUtils.isEmpty(response.getItems())) {
                return instances;
            }
            instances.addAll(getDbInstance(regionId, aliyun, response.getItems()));
            if (instances.size() >= response.getTotalRecordCount()) {
                return instances;
            }
            pageNumber++;
        }
    }

    private List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> getDbInstance(String regionId,
                                                                                        EdsAliyunConfigModel.Aliyun aliyun,
                                                                                        List<DescribeDBInstancesResponse.DBInstance> instances) throws ClientException {
        Iterator<DescribeDBInstancesResponse.DBInstance> iter = instances.iterator();
        List<String> ids = instances.stream()
                .map(DescribeDBInstancesResponse.DBInstance::getDBInstanceId)
                .collect(Collectors.toList());
        DescribeDBInstanceAttributeRequest request = new DescribeDBInstanceAttributeRequest();
        request.setDBInstanceId(Joiner.on(",")
                .join(ids));
        DescribeDBInstanceAttributeResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getItems();
    }

}
