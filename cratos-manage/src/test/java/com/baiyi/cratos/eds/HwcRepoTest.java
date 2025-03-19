package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.baiyi.cratos.eds.huaweicloud.repo.HwcObsRepo;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 16:23
 * &#064;Version 1.0
 */
public class HwcRepoTest extends BaseEdsTest<EdsHwcConfigModel.Hwc> {

    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void test1() {
        EdsHwcConfigModel.Hwc hwc = getConfig(27);
        HwcObsRepo.createBucket("af-south-1-los1a", hwc, "oceanbase-backup");
    }

}