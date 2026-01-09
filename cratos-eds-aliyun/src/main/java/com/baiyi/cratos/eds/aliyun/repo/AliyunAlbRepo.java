package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.alb20200616.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunAlbClient;
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
 * @Author baiyi
 * @Date 2024/3/28 17:38
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunAlbRepo {

    public static List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> listLoadBalancers(
            String endpoint, EdsConfigs.Aliyun aliyun) throws Exception {
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

    public static List<ListAclsResponseBody.ListAclsResponseBodyAcls> listAcls(String endpoint,
                                                                               EdsConfigs.Aliyun aliyun,
                                                                               String aclId) throws Exception {
        return listAcls(endpoint, aliyun, List.of(aclId));
    }

    public static List<ListAclsResponseBody.ListAclsResponseBodyAcls> listAcls(String endpoint,
                                                                               EdsConfigs.Aliyun aliyun,
                                                                               List<String> aclIds) throws Exception {
        ListAclsRequest request = new ListAclsRequest();
        request.setAclIds(aclIds);
        List<ListAclsResponseBody.ListAclsResponseBodyAcls> aclList = Lists.newArrayList();
        com.aliyun.alb20200616.Client client = AliyunAlbClient.createClient(endpoint, aliyun);
        String nextToken = "";
        do {
            request.setNextToken(nextToken);
            ListAclsResponse response = client.listAcls(request);
            List<ListAclsResponseBody.ListAclsResponseBodyAcls> results = Optional.ofNullable(response)
                    .map(ListAclsResponse::getBody)
                    .map(ListAclsResponseBody::getAcls)
                    .orElse(Collections.emptyList());
            if (!CollectionUtils.isEmpty(results)) {
                aclList.addAll(results);
            }
            nextToken = Optional.ofNullable(response)
                    .map(ListAclsResponse::getBody)
                    .map(ListAclsResponseBody::getNextToken)
                    .orElse("");
        } while (StringUtils.hasText(nextToken));
        return aclList;
    }

    public static List<ListAclEntriesResponseBody.ListAclEntriesResponseBodyAclEntries> listAclEntries(String endpoint,
                                                                                                       EdsConfigs.Aliyun aliyun,
                                                                                                       String aclId) throws Exception {
        ListAclEntriesRequest request = new ListAclEntriesRequest();
        request.setAclId(aclId);
        List<ListAclEntriesResponseBody.ListAclEntriesResponseBodyAclEntries> aclEntries = Lists.newArrayList();
        com.aliyun.alb20200616.Client client = AliyunAlbClient.createClient(endpoint, aliyun);
        String nextToken = "";
        do {
            request.setNextToken(nextToken);
            ListAclEntriesResponse response = client.listAclEntries(request);
            List<ListAclEntriesResponseBody.ListAclEntriesResponseBodyAclEntries> results = Optional.ofNullable(
                            response)
                    .map(ListAclEntriesResponse::getBody)
                    .map(ListAclEntriesResponseBody::getAclEntries)
                    .orElse(Collections.emptyList());
            if (!CollectionUtils.isEmpty(results)) {
                aclEntries.addAll(results);
            }
            nextToken = Optional.ofNullable(response)
                    .map(ListAclEntriesResponse::getBody)
                    .map(ListAclEntriesResponseBody::getNextToken)
                    .orElse("");
        } while (StringUtils.hasText(nextToken));
        return aclEntries;
    }

}
