package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
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
        edsFacade.importInstanceAsset(importInstanceAsset);
    }

    @Test
    void awsCertTest() {
        EdsInstanceParam.ImportInstanceAsset importInstanceAsset = EdsInstanceParam.ImportInstanceAsset.builder()
                .instanceId(94)
                .assetType(EdsAssetTypeEnum.AWS_CERT.name())
                .build();
        edsFacade.importInstanceAsset(importInstanceAsset);
    }

}
