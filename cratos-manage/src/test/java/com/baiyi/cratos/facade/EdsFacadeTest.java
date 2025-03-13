package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.identity.EdsIdentityFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/2/29 16:52
 * @Version 1.0
 */
public class EdsFacadeTest extends BaseUnit {

    @Resource
    private EdsFacade edsFacade;

    @Resource
    private EdsIdentityFacade edsIdentityFacade;

    @Test
    void aliyunCertTest() {
        EdsInstanceParam.ImportInstanceAsset importInstanceAsset = EdsInstanceParam.ImportInstanceAsset.builder()
                .instanceId(93)
                .assetType(EdsAssetTypeEnum.ALIYUN_CERT.name())
                .build();
        edsFacade.importEdsInstanceAsset(importInstanceAsset);
    }

    @Test
    void awsCertTest() {
        EdsInstanceParam.ImportInstanceAsset importInstanceAsset = EdsInstanceParam.ImportInstanceAsset.builder()
                .instanceId(94)
                .assetType(EdsAssetTypeEnum.AWS_CERT.name())
                .build();
        edsFacade.importEdsInstanceAsset(importInstanceAsset);
    }

    // @EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.CLOUDFLARE, assetType = EdsAssetTypeEnum.CLOUDFLARE_CERT)

    @Test
    void cfCertTest() {
        EdsInstanceParam.ImportInstanceAsset importInstanceAsset = EdsInstanceParam.ImportInstanceAsset.builder()
                .instanceId(95)
                .assetType(EdsAssetTypeEnum.CLOUDFLARE_CERT.name())
                .build();
        edsFacade.importEdsInstanceAsset(importInstanceAsset);
    }

    @Test
    void awsVpnTest() {
        EdsInstanceParam.ImportInstanceAsset importInstanceAsset = EdsInstanceParam.ImportInstanceAsset.builder()
                .instanceId(94)
                .assetType(EdsAssetTypeEnum.AWS_STS_VPN.name())
                .build();
        edsFacade.importEdsInstanceAsset(importInstanceAsset);
    }

    @Test
    void queryCloudIdentityDetailsTest() {
        EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails = EdsIdentityParam.QueryCloudIdentityDetails.builder()
                .username("baiyi")
                .build();
        EdsIdentityVO.CloudIdentityDetails details = edsIdentityFacade.queryCloudIdentityDetails(
                queryCloudIdentityDetails);
        System.out.println(details);
    }

    @Test
    void test() {
        EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails = EdsIdentityParam.QueryLdapIdentityDetails.builder()
                .username("baiyi")
                .build();
        EdsIdentityVO.LdapIdentityDetails details = edsIdentityFacade.queryLdapIdentityDetails(
                queryLdapIdentityDetails);
        System.out.println(details);
    }

    @Test
    void test2() {
        EdsIdentityParam.QueryDingtalkIdentityDetails query = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
                .username("baiyi")
                .build();
        EdsIdentityVO.DingtalkIdentityDetails details = edsIdentityFacade.queryDingtalkIdentityDetails(query);
        System.out.println(details);
    }

    @Test
    void test3() {
        EdsIdentityParam.QueryMailIdentityDetails query = EdsIdentityParam.QueryMailIdentityDetails.builder()
                .username("baiyi")
                .build();
        EdsIdentityVO.MailIdentityDetails details = edsIdentityFacade.queryMailIdentityDetails(query);
        System.out.println(details);
    }

}
