package com.baiyi.cratos.eds.cloudflare;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/8 10:13
 * &#064;Version 1.0
 */
public class EdsCloudFlareTest extends BaseEdsTest<EdsConfigs.Cloudflare> {

    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void test() {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(95, EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD.name());
        assets.forEach(asset -> {




        });
    }

}
