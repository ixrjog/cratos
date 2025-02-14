package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
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
    void test() {
        EdsInstanceParam.QueryCloudIdentityDetails queryCloudIdentityDetails = EdsInstanceParam.QueryCloudIdentityDetails.builder()
                .username("baiyi")
                .build();
        EdsAssetVO.CloudIdentityDetails details = edsFacade.queryCloudIdentityDetails(queryCloudIdentityDetails);
        System.out.println(details);
    }

}
