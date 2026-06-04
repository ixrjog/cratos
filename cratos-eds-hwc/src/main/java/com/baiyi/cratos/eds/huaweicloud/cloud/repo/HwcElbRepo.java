package com.baiyi.cratos.eds.huaweicloud.cloud.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.huaweicloud.cloud.client.HwcElbClientBuilder;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.elb.v3.ElbClient;
import com.huaweicloud.sdk.elb.v3.model.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/1 10:38
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class HwcElbRepo {

    private static final int MAX_LIMIT = 2000;

    public static List<LoadBalancer> listLoadBalancers(String regionId,
                                                       EdsConfigs.Hwc huaweicloud) throws ServiceResponseException {
        List<LoadBalancer> loadBalancerList = Lists.newArrayList();
        ElbClient client = HwcElbClientBuilder.buildElbClient(regionId, huaweicloud);
        ListLoadBalancersRequest request = new ListLoadBalancersRequest().withLimit(MAX_LIMIT);
        //.withProtectionStatus(List.of("ACTIVE"))
        //.withAdminStateUp(true);
        while (true) {
            ListLoadBalancersResponse response = client.listLoadBalancers(request);
            loadBalancerList.addAll(response.getLoadbalancers());
            String nextMarker = Optional.of(response)
                    .map(ListLoadBalancersResponse::getPageInfo)
                    .map(PageInfo::getNextMarker)
                    .orElse(null);

            if (!StringUtils.hasText(nextMarker)) {
                break;
            } else {
                request.setMarker(nextMarker);
            }
        }
        return loadBalancerList;
    }

    public static List<Listener> listListeners(String regionId, EdsConfigs.Hwc huaweicloud,
                                               String loadbalancerId) throws ServiceResponseException {
        List<Listener> listenerList = Lists.newArrayList();
        ElbClient client = HwcElbClientBuilder.buildElbClient(regionId, huaweicloud);
        ListListenersRequest request = new ListListenersRequest().withLimit(MAX_LIMIT)
                .withLoadbalancerId(List.of(loadbalancerId))
                .withAdminStateUp(true);
        while (true) {
            ListListenersResponse response = client.listListeners(request);
            listenerList.addAll(response.getListeners());
            String nextMarker = Optional.of(response)
                    .map(ListListenersResponse::getPageInfo)
                    .map(PageInfo::getNextMarker)
                    .orElse(null);

            if (!StringUtils.hasText(nextMarker)) {
                break;
            } else {
                request.setMarker(nextMarker);
            }
        }
        return listenerList;
    }

    public static Pool showPool(String regionId, EdsConfigs.Hwc huaweicloud,
                                String poolId) throws ServiceResponseException {
        ElbClient client = HwcElbClientBuilder.buildElbClient(regionId, huaweicloud);
        ShowPoolRequest request = new ShowPoolRequest().withPoolId(poolId);
        ShowPoolResponse response = client.showPool(request);
        return response.getPool();
    }

    public static List<Member> listMembers(String regionId, EdsConfigs.Hwc huaweicloud,
                                           String poolId) throws ServiceResponseException {
        ElbClient client = HwcElbClientBuilder.buildElbClient(regionId, huaweicloud);
        ListMembersRequest request = new ListMembersRequest().withPoolId(poolId);
        ListMembersResponse response = client.listMembers(request);
        return response.getMembers();
    }

    public static List<CertificateInfo> listCertificates(String regionId,
                                                         EdsConfigs.Hwc huaweicloud) throws ServiceResponseException {
        List<CertificateInfo> certificatesList = Lists.newArrayList();
        ElbClient client = HwcElbClientBuilder.buildElbClient(regionId, huaweicloud);
        ListCertificatesRequest request = new ListCertificatesRequest().withLimit(MAX_LIMIT);
        while (true) {
            ListCertificatesResponse response = client.listCertificates(request);
            certificatesList.addAll(response.getCertificates());
            String nextMarker = Optional.of(response)
                    .map(ListCertificatesResponse::getPageInfo)
                    .map(PageInfo::getNextMarker)
                    .orElse(null);

            if (!StringUtils.hasText(nextMarker)) {
                break;
            } else {
                request.setMarker(nextMarker);
            }
        }
        return certificatesList;
    }

    public static CertificateInfo createCertificate(String regionId, EdsConfigs.Hwc huaweicloud, String certName,
                                                    String domain, String cert, String privateKey) {
        ElbClient client = HwcElbClientBuilder.buildElbClient(regionId, huaweicloud);
        CreateCertificateOption certificate = new CreateCertificateOption().withName(certName)
                .withCertificate(cert)
                .withDomain(domain)
                .withPrivateKey(privateKey)
                .withType(CreateCertificateOption.TypeEnum.SERVER);

        CreateCertificateRequestBody body = new CreateCertificateRequestBody().withCertificate(certificate);
        CreateCertificateRequest request = new CreateCertificateRequest().withBody(body);
        return Optional.ofNullable(client.createCertificate(request))
                .map(CreateCertificateResponse::getCertificate)
                .orElse(null);
    }

}
