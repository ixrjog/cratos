package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.cratos.eds.aliyun.client.common.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/5 下午3:23
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRdsDatabaseRepo {

    private final AliyunClient aliyunClient;

    public static final int PAGE_SIZE = 50;

    /**
     * 查询数据库实例中的所有数据
     * https://help.aliyun.com/document_detail/26260.html
     *
     * @param regionId
     * @param aliyun
     * @param dbInstanceId
     * @return
     */
    public List<DescribeDatabasesResponse.Database> listDatabase(String regionId, EdsConfigs.Aliyun aliyun, String dbInstanceId) throws ClientException {
        DescribeDatabasesRequest describe = new DescribeDatabasesRequest();
        describe.setDBInstanceId(dbInstanceId);
        describe.setPageSize(PAGE_SIZE);
        int size = PAGE_SIZE;
        List<DescribeDatabasesResponse.Database> databases = Lists.newArrayList();
        int pageNumber = 1;
        // 返回值无总数，使用其它算法取所有数据库
        while (PAGE_SIZE <= size) {
            describe.setPageNumber(pageNumber);
            DescribeDatabasesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
            databases.addAll(response.getDatabases());
            size = response.getDatabases().size();
            pageNumber++;
        }
        return databases;
    }

}
