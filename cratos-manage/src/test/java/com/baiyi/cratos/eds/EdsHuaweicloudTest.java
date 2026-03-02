package com.baiyi.cratos.eds;

import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.*;
import com.baiyi.cratos.service.acme.AcmeCertificateService;
import com.baiyi.cratos.service.acme.AcmeDomainService;
import com.huaweicloud.sdk.ccm.v1.model.Certificates;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/16 09:50
 * &#064;Version 1.0
 */
public class EdsHuaweicloudTest extends BaseEdsTest<EdsConfigs.Hwc> {

    @Resource
    private AcmeCertificateService acmeCertificateService;
    @Autowired
    private AcmeDomainService acmeDomainService;

    @Test
    void ecsTest() {
        EdsConfigs.Hwc cfg = getConfig(27);
        List<ServerDetail> serverDetails = HwcEcsRepo.listServers("eu-west-101", cfg);
        System.out.println(serverDetails);
    }

    @Test
    void iamTest() {
        EdsConfigs.Hwc cfg = getConfig(27);
        List<KeystoneListUsersResult> usersResults = HwcIamRepo.listUsers(cfg);
        System.out.println(usersResults);
    }

    @Test
    void certTest() {
        EdsConfigs.Hwc cfg = getConfig(27);
        List<Certificates> certificates = HwcCcmRepo.listCertificates("eu-west-101", cfg);
        System.out.println(certificates);
    }

    public static final Region CN_NORTH_4 = new Region("cn-north-4", "https://scm.cn-north-4.myhuaweicloud.com");
    public static final Region AP_SOUTHEAST_1 = new Region(
            "ap-southeast-1", "https://scm.ap-southeast-1.myhuaweicloud.com");

    @Test
    void scmTest() {
        EdsConfigs.Hwc cfg = getConfig(27);
        List<com.huaweicloud.sdk.scm.v3.model.CertificateDetail> certificates = HwcScmRepo.listCertificates(
                "eu-west-101", cfg);
        System.out.println(certificates);
    }

    @Test
    void certTest2() {
        AcmeCertificate acmeCertificate = acmeCertificateService.getById(11);

        AcmeDomain acmeDomain = acmeDomainService.getById(acmeCertificate.getDomainId());

        EdsConfigs.Hwc cfg = getConfig(27);

        String cert = acmeCertificate.getCertificate() + "\n" + acmeCertificate.getCertificateChain();

        HwcElbRepo.createCertificate(
                "eu-west-101", cfg, "test", acmeCertificate.getDomains(), cert, acmeCertificate.getPrivateKey());

    }

}
