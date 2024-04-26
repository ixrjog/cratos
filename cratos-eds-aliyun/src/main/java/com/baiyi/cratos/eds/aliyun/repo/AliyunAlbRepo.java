package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.alb20200616.models.ListLoadBalancersRequest;
import com.aliyun.alb20200616.models.ListLoadBalancersResponse;
import com.aliyun.alb20200616.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.eds.aliyun.client.AliyunAlbClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/3/28 17:38
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunAlbRepo {

    public List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> listAlb(String endpoint, EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        ListLoadBalancersRequest request = new ListLoadBalancersRequest();
        List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> albList = Lists.newArrayList();
        com.aliyun.alb20200616.Client client = AliyunAlbClient.createClient(endpoint, aliyun);
        while (true) {
            ListLoadBalancersResponse response = client.listLoadBalancers(request);
            response.getBody()
                    .getLoadBalancers();
            List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> results = Optional.of(response)
                    .map(ListLoadBalancersResponse::getBody)
                    .map(ListLoadBalancersResponseBody::getLoadBalancers)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(results)) {
                break;
            } else {
                albList.addAll(results);
            }
            String nextToken = Optional.of(response)
                    .map(ListLoadBalancersResponse::getBody)
                    .map(ListLoadBalancersResponseBody::getNextToken)
                    .orElse("");
            if (StringUtils.hasText(nextToken)) {
                request.setNextToken(nextToken);
            } else {
                break;
            }
        }
        return albList;
    }

}
