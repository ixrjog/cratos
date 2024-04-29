package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.sdk.service.domain20180129.AsyncClient;
import com.aliyun.sdk.service.domain20180129.models.QueryDomainListRequest;
import com.aliyun.sdk.service.domain20180129.models.QueryDomainListResponse;
import com.aliyun.sdk.service.domain20180129.models.QueryDomainListResponseBody;
import com.baiyi.cratos.eds.aliyun.client.AliyunDomainAsyncClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @Author baiyi
 * @Date 2024/4/26 上午10:17
 * @Version 1.0
 */
@Slf4j
public class AliyunDomainRepo {

    public static List<QueryDomainListResponseBody.Domain> listDomain(EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        int pageNum = 1;
        List<QueryDomainListResponseBody.Domain> domainList = Lists.newArrayList();
        try (AsyncClient client = AliyunDomainAsyncClient.createClient(aliyun)) {
            while (true) {
                QueryDomainListRequest request = QueryDomainListRequest.builder()
                        .pageNum(pageNum)
                        .pageSize(10)
                        .build();
                CompletableFuture<QueryDomainListResponse> completableFutureResponse = client.queryDomainList(request);
                QueryDomainListResponse response = completableFutureResponse.get();
                Optional<QueryDomainListResponseBody.Data> optionalData = Optional.ofNullable(response)
                        .map(QueryDomainListResponse::getBody)
                        .map(QueryDomainListResponseBody::getData);
                if (optionalData.isPresent()) {
                    QueryDomainListResponseBody.Data data = optionalData.get();
                    if (CollectionUtils.isEmpty(data.getDomain())) {
                        break;
                    }
                    domainList.addAll(data.getDomain());
                    if (response.getBody()
                            .getNextPage()) {
                        pageNum++;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return domainList;
    }

}
