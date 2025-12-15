package com.baiyi.cratos.eds.aliyun.repo;


import com.aliyun.nlb20220430.models.ListLoadBalancersRequest;
import com.aliyun.nlb20220430.models.ListLoadBalancersResponse;
import com.aliyun.nlb20220430.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.eds.aliyun.client.AliyunNlbClient;
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
 * &#064;Author  baiyi
 * &#064;Date  2025/9/4 15:08
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunNlbRepo {

    public static List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> listLoadBalancers(
            String endpoint, EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        ListLoadBalancersRequest request = new ListLoadBalancersRequest();
        List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> nlbList = Lists.newArrayList();
        com.aliyun.nlb20220430.Client client = AliyunNlbClient.createClient(endpoint, aliyun);
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
                nlbList.addAll(results);
            }
            nextToken = Optional.ofNullable(response)
                    .map(ListLoadBalancersResponse::getBody)
                    .map(ListLoadBalancersResponseBody::getNextToken)
                    .orElse("");
        } while (StringUtils.hasText(nextToken));
        return nlbList;
    }

}
