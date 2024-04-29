package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.cas20200407.Client;
import com.aliyun.cas20200407.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunClient;
import com.baiyi.cratos.eds.aliyun.client.AliyunOpenapiClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.query.EdsRepoData;
import com.baiyi.cratos.eds.core.query.EdsRepoPageQuery;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/2/26 10:38
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunCertRepo {

    private final AliyunClient aliyunClient;

    public List<ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> listUserCertOrder(
            EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        ListUserCertificateOrderRequest request = new ListUserCertificateOrderRequest();
        long total = 0;
        long pageNo = 1;
        List<ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> userCertificateOrderList = Lists.newArrayList();
        while (total == 0 || total == userCertificateOrderList.size()) {
            request.setCurrentPage(pageNo);
            Client aliyunClient = AliyunOpenapiClient.createClient(aliyun);
            ListUserCertificateOrderResponse response = aliyunClient.listUserCertificateOrder(request);
            List<ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> results = Optional.of(
                            response)
                    .map(ListUserCertificateOrderResponse::getBody)
                    .map(ListUserCertificateOrderResponseBody::getCertificateOrderList)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(results)) {
                break;
            } else {
                userCertificateOrderList.addAll(results);
            }
            if (total == 0) {
                total = Optional.of(response)
                        .map(ListUserCertificateOrderResponse::getBody)
                        .map(ListUserCertificateOrderResponseBody::getTotalCount)
                        .orElse(0L);
            }
            pageNo++;
        }
        return userCertificateOrderList;
    }

    public List<ListCertResponseBody.ListCertResponseBodyCertList> listCert(
            EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        ListCertRequest request = new ListCertRequest();
        long total = 0;
        long pageNo = 1;
        List<ListCertResponseBody.ListCertResponseBodyCertList> certList = Lists.newArrayList();
        while (total == 0 || total == certList.size()) {
            request.setCurrentPage(pageNo);
            Client aliyunClient = AliyunOpenapiClient.createClient(aliyun);
            ListCertResponse response = aliyunClient.listCert(request);
            List<ListCertResponseBody.ListCertResponseBodyCertList> results = Optional.of(response)
                    .map(ListCertResponse::getBody)
                    .map(ListCertResponseBody::getCertList)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(results)) {
                break;
            } else {
                certList.addAll(results);
            }
            if (total == 0) {
                total = Optional.of(response)
                        .map(ListCertResponse::getBody)
                        .map(ListCertResponseBody::getTotalCount)
                        .orElse(0L);
            }
            pageNo++;
        }
        return certList;
    }

    public EdsRepoData<ListCertResponseBody.ListCertResponseBodyCertList> queryCertPage(
            EdsAliyunConfigModel.Aliyun aliyun, EdsRepoPageQuery edsRepoPageQuery) throws Exception {
        ListCertRequest request = new ListCertRequest();
        request.setCurrentPage(edsRepoPageQuery.getPage());
        request.setShowSize(edsRepoPageQuery.getLength());
        Client aliyunClient = AliyunOpenapiClient.createClient(aliyun);
        ListCertResponse response = aliyunClient.listCert(request);
        List<ListCertResponseBody.ListCertResponseBodyCertList> results = Optional.of(response)
                .map(ListCertResponse::getBody)
                .map(ListCertResponseBody::getCertList)
                .orElse(Collections.emptyList());

        return EdsRepoData.<ListCertResponseBody.ListCertResponseBodyCertList>builder()
                .nowPage(edsRepoPageQuery.getPage())
                .totalNum(Optional.of(response)
                        .map(ListCertResponse::getBody)
                        .map(ListCertResponseBody::getTotalCount)
                        .orElse(0L))
                .data(results)
                .build();
    }

}
