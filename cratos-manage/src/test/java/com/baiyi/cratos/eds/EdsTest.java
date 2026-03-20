package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/20 10:15
 * &#064;Version 1.0
 */
public class EdsTest extends BaseUnit {

    @Resource
    private EdsFacade edsFacade;
    @Autowired
    private EdsAssetService edsAssetService;


    @Test
    void test() {
        // 149 IDC BD JSR Robi Cloud
        // 150 IDC BD Dhaka Coloasia Ltd
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(126, EdsAssetTypeEnum.CRATOS_COMPUTER.name());
        for (EdsAsset asset : assets) {
            if (asset.getName()
                    .startsWith("pk-isb-")) {
                edsFacade.migrateAsset(asset.getId(), 152, EdsAssetTypeEnum.CUSTOM_IDC_HOST.name());
                System.out.println(asset.getName());
            }
        }

        // edsFacade.migrateAsset()

    }


}
