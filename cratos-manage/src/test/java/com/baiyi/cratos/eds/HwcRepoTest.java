package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcElbRepo;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcObsRepo;
import com.baiyi.cratos.service.EdsAssetService;
import com.huaweicloud.sdk.elb.v3.model.CertificateInfo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 16:23
 * &#064;Version 1.0
 */
public class HwcRepoTest extends BaseEdsTest<EdsConfigs.Hwc> {

    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void test1() {
        EdsConfigs.Hwc hwc = getConfig(27);
        HwcObsRepo.createBucket("af-south-1-los1a", hwc, "oceanbase-backup");
    }

    @Test
    void test2() {
        EdsConfigs.Hwc hwc = getConfig(27);
        // eu-west-101 af-south-1
        List<CertificateInfo> list = HwcElbRepo.listCertificates("eu-west-101", hwc);
        System.out.println(list);
    }

}