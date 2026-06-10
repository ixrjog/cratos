package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.slb20140515.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunCLBClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/2 17:07
 * &#064;Version 1.0
 */
public class AliyunCLBRepo {

    private static final int PAGE_SIZE = 100;

    /**
     * https://help.aliyun.com/zh/slb/classic-load-balancer/developer-reference/api-slb-2014-05-15-describeloadbalancers?spm=a2c4g.11186623.help-menu-27537.d_4_0_4_1_11.542f2ff4oVjRjx&scm=20140722.H_2401696._.OR_help-T_cn~zh-V_1
     *
     * @param endpoint
     * @param aliyun
     * @return
     * @throws Exception
     */
    public static List<DescribeLoadBalancersResponseBody.DescribeLoadBalancersResponseBodyLoadBalancersLoadBalancer> describeLoadBalancers(
            String endpoint, EdsConfigs.Aliyun aliyun) throws Exception {
        List<DescribeLoadBalancersResponseBody.DescribeLoadBalancersResponseBodyLoadBalancersLoadBalancer> clbList = Lists.newArrayList();
        com.aliyun.slb20140515.Client client = AliyunCLBClient.createClient(endpoint, aliyun);
        int pageNumber = 1;
        int totalCount;
        do {
            DescribeLoadBalancersRequest request = new DescribeLoadBalancersRequest().setPageNumber(pageNumber)
                    .setPageSize(PAGE_SIZE);
            DescribeLoadBalancersResponse response = client.describeLoadBalancers(request);
            List<DescribeLoadBalancersResponseBody.DescribeLoadBalancersResponseBodyLoadBalancersLoadBalancer> result = Optional.ofNullable(
                            response)
                    .map(DescribeLoadBalancersResponse::getBody)
                    .map(DescribeLoadBalancersResponseBody::getLoadBalancers)
                    .map(DescribeLoadBalancersResponseBody.DescribeLoadBalancersResponseBodyLoadBalancers::getLoadBalancer)
                    .orElse(List.of());

            clbList.addAll(result);
            totalCount = Optional.ofNullable(response)
                    .map(DescribeLoadBalancersResponse::getBody)
                    .map(DescribeLoadBalancersResponseBody::getTotalCount)
                    .orElse(0);
            pageNumber++;
        } while (clbList.size() < totalCount);
        return clbList;
    }

    public static DescribeLoadBalancerAttributeResponseBody describeLoadBalancerAttribute(String endpoint,
                                                                                          EdsConfigs.Aliyun aliyun,
                                                                                          String loadBalancerId) throws Exception {
        com.aliyun.slb20140515.Client client = AliyunCLBClient.createClient(endpoint, aliyun);
        DescribeLoadBalancerAttributeRequest request = new DescribeLoadBalancerAttributeRequest();
        request.setLoadBalancerId(loadBalancerId);
        return Optional.ofNullable(client.describeLoadBalancerAttribute(request))
                .map(DescribeLoadBalancerAttributeResponse::getBody)
                .orElse(null);
    }

    public static List<DescribeLoadBalancerListenersResponseBody.DescribeLoadBalancerListenersResponseBodyListeners> describeLoadBalancerListeners(
            String endpoint, EdsConfigs.Aliyun aliyun, String loadBalancerId) throws Exception {
        DescribeLoadBalancerListenersRequest request = new DescribeLoadBalancerListenersRequest();
        request.setLoadBalancerId(List.of(loadBalancerId));
        request.setMaxResults(PAGE_SIZE);
        com.aliyun.slb20140515.Client client = AliyunCLBClient.createClient(endpoint, aliyun);
        List<DescribeLoadBalancerListenersResponseBody.DescribeLoadBalancerListenersResponseBodyListeners> listeners = Lists.newArrayList();
        String nextToken = "";
        do {
            request.setNextToken(nextToken);
            DescribeLoadBalancerListenersResponse response = client.describeLoadBalancerListeners(request);
            List<DescribeLoadBalancerListenersResponseBody.DescribeLoadBalancerListenersResponseBodyListeners> results = Optional.ofNullable(
                            response)
                    .map(DescribeLoadBalancerListenersResponse::getBody)
                    .map(DescribeLoadBalancerListenersResponseBody::getListeners)
                    .orElse(Collections.emptyList());
            if (!CollectionUtils.isEmpty(results)) {
                listeners.addAll(results);
            }
            nextToken = Optional.ofNullable(response)
                    .map(DescribeLoadBalancerListenersResponse::getBody)
                    .map(DescribeLoadBalancerListenersResponseBody::getNextToken)
                    .orElse("");
        } while (StringUtils.hasText(nextToken));
        return listeners;
    }

    public static List<DescribeVServerGroupAttributeResponseBody.DescribeVServerGroupAttributeResponseBodyBackendServersBackendServer> describeVServerGroupAttribute(
            String endpoint, EdsConfigs.Aliyun aliyun, String vServerGroupId) throws Exception {
        DescribeVServerGroupAttributeRequest request = new DescribeVServerGroupAttributeRequest();
        request.setVServerGroupId(vServerGroupId);
        com.aliyun.slb20140515.Client client = AliyunCLBClient.createClient(endpoint, aliyun);
        DescribeVServerGroupAttributeResponse response = client.describeVServerGroupAttribute(request);
        return Optional.ofNullable(response)
                .map(DescribeVServerGroupAttributeResponse::getBody)
                .map(DescribeVServerGroupAttributeResponseBody::getBackendServers)
                .map(DescribeVServerGroupAttributeResponseBody.DescribeVServerGroupAttributeResponseBodyBackendServers::getBackendServer)
                .orElse(Collections.emptyList());
    }

}
