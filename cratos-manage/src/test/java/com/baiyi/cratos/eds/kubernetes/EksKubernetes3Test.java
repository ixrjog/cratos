package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.service.EdsAssetService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/15 11:01
 * &#064;Version 1.0
 */
public class EksKubernetes3Test extends BaseEdsTest<EdsConfigs.Kubernetes> {
    @Autowired
    private EdsAssetService edsAssetService;
    @Autowired
    private EdsProviderHolderFactory edsProviderHolderFactory;

    // 101

    @Test
    void test() {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(101, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());


        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> holder = (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment>)  edsProviderHolderFactory
                .createHolder(101, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());


    }

}
