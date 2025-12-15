package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.sdk.service.domain20180129.AsyncClient;
import com.aliyun.sdk.service.domain20180129.models.QueryDomainListRequest;
import com.aliyun.sdk.service.domain20180129.models.QueryDomainListResponse;
import com.aliyun.sdk.service.domain20180129.models.QueryDomainListResponseBody;
import com.baiyi.cratos.eds.aliyun.client.AliyunDomainAsyncClient;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/4/26 上午10:17
 * @Version 1.0
 */
@Slf4j
public class AliyunDomainRepo {

    public static List<QueryDomainListResponseBody.Domain> listDomain(
            EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        int pageNum = 1;
        List<QueryDomainListResponseBody.Domain> domainList = Lists.newArrayList();
        try (AsyncClient client = AliyunDomainAsyncClient.createClient(aliyun)) {
            boolean hasNextPage;
            do {
                QueryDomainListRequest request = QueryDomainListRequest.builder()
                        .pageNum(pageNum)
                        .pageSize(10)
                        .build();
                QueryDomainListResponse response = client.queryDomainList(request)
                        .get();
                QueryDomainListResponseBody.Data data = Optional.ofNullable(response)
                        .map(QueryDomainListResponse::getBody)
                        .map(QueryDomainListResponseBody::getData)
                        .orElse(null);
                if (data == null || CollectionUtils.isEmpty(data.getDomain())) {
                    break;
                }
                domainList.addAll(data.getDomain());
                hasNextPage = Boolean.TRUE.equals(response.getBody()
                        .getNextPage());
                pageNum++;
            } while (hasNextPage);
        } catch (Exception e) {
            log.error("Failed to list domains: {}", e.getMessage(), e);
            throw e;
        }
        return domainList;
    }

}
