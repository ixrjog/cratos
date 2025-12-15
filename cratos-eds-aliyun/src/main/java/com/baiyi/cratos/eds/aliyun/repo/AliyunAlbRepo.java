package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.alb20200616.models.ListLoadBalancersRequest;
import com.aliyun.alb20200616.models.ListLoadBalancersResponse;
import com.aliyun.alb20200616.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.eds.aliyun.client.AliyunAlbClient;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/28 17:38
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunAlbRepo {

    public static List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> listLoadBalancers(
            String endpoint, EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        ListLoadBalancersRequest request = new ListLoadBalancersRequest();
        List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> albList = Lists.newArrayList();
        com.aliyun.alb20200616.Client client = AliyunAlbClient.createClient(endpoint, aliyun);
        String nextToken = "";
        do {
            request.setNextToken(nextToken);
            ListLoadBalancersResponse response = client.listLoadBalancers(request);
            List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> results = Optional.ofNullable(
                            response)
                    .map(ListLoadBalancersResponse::getBody)
                    .map(ListLoadBalancersResponseBody::getLoadBalancers)
                    .orElse(Collections.emptyList());
            if (!CollectionUtils.isEmpty(results)) {
                albList.addAll(results);
            }
            nextToken = Optional.ofNullable(response)
                    .map(ListLoadBalancersResponse::getBody)
                    .map(ListLoadBalancersResponseBody::getNextToken)
                    .orElse("");
        } while (StringUtils.hasText(nextToken));
        return albList;
    }

}
