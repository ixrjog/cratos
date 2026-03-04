package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.cas20200407.Client;
import com.aliyun.cas20200407.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunCasClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.query.EdsRepoData;
import com.baiyi.cratos.eds.core.query.EdsRepoPageQuery;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    public List<ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> listUserCertOrder(
            EdsConfigs.Aliyun aliyun) throws Exception {
        ListUserCertificateOrderRequest request = new ListUserCertificateOrderRequest();
        long total = 0;
        long pageNo = 1;
        List<ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> userCertificateOrderList = Lists.newArrayList();
        while (total == 0 || total == userCertificateOrderList.size()) {
            request.setCurrentPage(pageNo);
            Client aliyunClient = AliyunCasClient.createClient(aliyun);
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

    public List<ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> listCertificates(
            EdsConfigs.Aliyun aliyun) throws Exception {

        List<ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> certList = new ArrayList<>();
        Client aliyunClient = AliyunCasClient.createClient(aliyun);
        int currentPage = 1;
        int showSize = 100;
        long totalCount = 0;
        do {
            ListCertificatesRequest request = new ListCertificatesRequest().setCurrentPage(currentPage)
                    .setShowSize(showSize);
            ListCertificatesResponse response = aliyunClient.listCertificates(request);
            ListCertificatesResponseBody body = response.getBody();
            if (body == null || CollectionUtils.isEmpty(body.getCertificateList())) {
                break;
            }
            certList.addAll(body.getCertificateList());
            if (totalCount == 0) {
                totalCount = body.getTotalCount();
            }
            currentPage++;
        } while (certList.size() < totalCount);
        return certList;
    }

    /**
     * 按名称查询证书
     *
     * @param aliyun
     * @param certificateName
     * @return
     * @throws Exception
     */
    public ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList queryCertificateByName(
            EdsConfigs.Aliyun aliyun, String certificateName) throws Exception {
        ListCertificatesRequest request = new ListCertificatesRequest().setKeyword(certificateName)
                .setShowSize(100);
        Client aliyunClient = AliyunCasClient.createClient(aliyun);
        int currentPage = 1;
        int maxPages = 100; // 防止无限循环
        while (currentPage <= maxPages) {
            request.setCurrentPage(currentPage);
            ListCertificatesResponse response = aliyunClient.listCertificates(request);
            List<ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> certificates = Optional.ofNullable(
                            response)
                    .map(ListCertificatesResponse::getBody)
                    .map(ListCertificatesResponseBody::getCertificateList)
                    .orElse(List.of());

            if (CollectionUtils.isEmpty(certificates)) {
                break;
            }
            Optional<ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> result = certificates.stream()
                    .filter(cert -> certificateName.equals(cert.getCertificateName()))
                    .findFirst();
            if (result.isPresent()) {
                return result.get();
            }
            currentPage++;
        }
        return null;
    }

    public EdsRepoData<ListCertResponseBody.ListCertResponseBodyCertList> queryCertPage(EdsConfigs.Aliyun aliyun,
                                                                                        EdsRepoPageQuery edsRepoPageQuery) throws Exception {
        ListCertRequest request = new ListCertRequest();
        request.setCurrentPage(edsRepoPageQuery.getPage());
        request.setShowSize(edsRepoPageQuery.getLength());
        Client aliyunClient = AliyunCasClient.createClient(aliyun);
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

    /**
     * 上传证书
     *
     * @param aliyun
     * @param name
     * @param cert
     * @param key
     * @return certId
     * @throws Exception
     */
    public Long uploadUserCertificate(EdsConfigs.Aliyun aliyun, String name, String cert, String key) throws Exception {
        UploadUserCertificateRequest request = new UploadUserCertificateRequest().setName(name)
                .setCert(cert)
                .setKey(key);
        Client aliyunClient = AliyunCasClient.createClient(aliyun);
        UploadUserCertificateResponse response = aliyunClient.uploadUserCertificate(request);
        return Optional.ofNullable(response)
                .map(UploadUserCertificateResponse::getBody)
                .map(UploadUserCertificateResponseBody::getCertId)
                .orElseThrow();
    }

    /**
     * 获取云厂商及对应云产品的资源列表
     * https://help.aliyun.com/zh/ssl-certificate/developer-reference/api-cas-2020-04-07-listcloudresources?spm=a2c4g.11186623.0.0.465677eatJnGDq
     *
     * @param aliyun
     * @param certId
     * @return
     * @throws Exception
     */
    public List<ListCloudResourcesResponseBody.ListCloudResourcesResponseBodyData> listCloudResources(
            EdsConfigs.Aliyun aliyun, Long certId) throws Exception {
        List<ListCloudResourcesResponseBody.ListCloudResourcesResponseBodyData> result = Lists.newArrayList();
        Client aliyunClient = AliyunCasClient.createClient(aliyun);
        int currentPage = 1;
        int showSize = 20;
        Long totalCount;
        do {
            ListCloudResourcesRequest request = new ListCloudResourcesRequest().setCurrentPage(currentPage)
                    .setShowSize(showSize)
                    .setCertIds(List.of(certId));
            ListCloudResourcesResponse response = aliyunClient.listCloudResources(request);
            ListCloudResourcesResponseBody body = response.getBody();
            if (body == null || CollectionUtils.isEmpty(body.getData())) {
                break;
            }
            result.addAll(body.getData());
            totalCount = body.getTotal();
            currentPage++;
        } while (result.size() < totalCount);
        return result;
    }

}
