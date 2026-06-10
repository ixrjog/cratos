package com.baiyi.cratos.eds.aliyun.repo;


import com.aliyun.nlb20220430.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunNLBClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
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
public class AliyunNLBRepo {

    public static List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> listLoadBalancers(
            String endpoint, EdsConfigs.Aliyun aliyun) throws Exception {
        ListLoadBalancersRequest request = new ListLoadBalancersRequest();
        List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> nlbList = Lists.newArrayList();
        com.aliyun.nlb20220430.Client client = AliyunNLBClient.createClient(endpoint, aliyun);
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

    /**
     * 查询监听
     *
     * @param endpoint
     * @param aliyun
     * @param loadBalancerId
     * @return
     * @throws Exception
     */
    public static List<ListListenersResponseBody.ListListenersResponseBodyListeners> listListeners(String endpoint,
                                                                                                   EdsConfigs.Aliyun aliyun,
                                                                                                   String loadBalancerId) throws Exception {
        ListListenersRequest request = new ListListenersRequest();
        request.setLoadBalancerIds(List.of(loadBalancerId));
        request.setMaxResults(100);
        com.aliyun.nlb20220430.Client client = AliyunNLBClient.createClient(endpoint, aliyun);
        List<ListListenersResponseBody.ListListenersResponseBodyListeners> listeners = Lists.newArrayList();
        String nextToken = "";
        do {
            request.setNextToken(nextToken);

            ListListenersResponse response = client.listListeners(request);
            List<ListListenersResponseBody.ListListenersResponseBodyListeners> results = Optional.ofNullable(response)
                    .map(ListListenersResponse::getBody)
                    .map(ListListenersResponseBody::getListeners)
                    .orElse(Collections.emptyList());
            if (!CollectionUtils.isEmpty(results)) {
                listeners.addAll(results);
            }
            nextToken = Optional.ofNullable(response)
                    .map(ListListenersResponse::getBody)
                    .map(ListListenersResponseBody::getNextToken)
                    .orElse("");
        } while (StringUtils.hasText(nextToken));
        return listeners;
    }

    public static List<ListServerGroupServersResponseBody.ListServerGroupServersResponseBodyServers> listServerGroupServers(
            String endpoint, EdsConfigs.Aliyun aliyun, String serverGroupId) throws Exception {

        ListServerGroupServersRequest request = new ListServerGroupServersRequest();
        request.setServerGroupId(serverGroupId);
        request.setMaxResults(100);
        com.aliyun.nlb20220430.Client client = AliyunNLBClient.createClient(endpoint, aliyun);
        List<ListServerGroupServersResponseBody.ListServerGroupServersResponseBodyServers> servers = Lists.newArrayList();
        String nextToken = "";
        do {
            request.setNextToken(nextToken);
            ListServerGroupServersResponse response = client.listServerGroupServers(request);
            List<ListServerGroupServersResponseBody.ListServerGroupServersResponseBodyServers> results = Optional.ofNullable(
                            response)
                    .map(ListServerGroupServersResponse::getBody)
                    .map(ListServerGroupServersResponseBody::getServers)
                    .orElse(Collections.emptyList());
            if (!CollectionUtils.isEmpty(results)) {
                servers.addAll(results);
            }
            nextToken = Optional.ofNullable(response)
                    .map(ListServerGroupServersResponse::getBody)
                    .map(ListServerGroupServersResponseBody::getNextToken)
                    .orElse("");
        } while (StringUtils.hasText(nextToken));
        return servers;
    }

}
