package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareCertRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareZoneRepo;
import com.baiyi.cratos.eds.core.config.model.EdsCloudflareConfigModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:57
 * @Version 1.0
 */
public class EdsCloudflareTest extends BaseEdsTest<EdsCloudflareConfigModel.Cloudflare> {

    @Resource
    private CloudflareZoneRepo cloudflareZoneRepo;

    @Resource
    private CloudflareCertRepo cloudflareCertRepo;

    @Test
    void zoneTest() {
        EdsCloudflareConfigModel.Cloudflare cf = getConfig(5);
        List<CloudflareZone.Zone> rt = cloudflareZoneRepo.listZones(cf);
        System.out.println(rt);
    }

    @Test
    void certTest() {
        EdsCloudflareConfigModel.Cloudflare cf = getConfig(5);
        List<CloudflareCert.Result> rt = cloudflareCertRepo.listCertificatePacks(cf, "5243357f773b873952f7f99090841934");
        System.out.println(rt);
    }

}
