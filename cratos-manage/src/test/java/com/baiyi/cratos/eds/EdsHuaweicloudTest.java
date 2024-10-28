package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudCcmRepo;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudEcsRepo;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudIamRepo;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudScmRepo;
import com.huaweicloud.sdk.ccm.v1.model.Certificates;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersResult;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/16 09:50
 * &#064;Version 1.0
 */
public class EdsHuaweicloudTest extends BaseEdsTest<EdsHuaweicloudConfigModel.Huaweicloud> {

    @Test
    void ecsTest() {
        EdsHuaweicloudConfigModel.Huaweicloud cfg = getConfig(27);
        List<ServerDetail> serverDetails = HuaweicloudEcsRepo.listServers("eu-west-101", cfg);
        System.out.println(serverDetails);
    }

    @Test
    void iamTest() {
        EdsHuaweicloudConfigModel.Huaweicloud cfg = getConfig(27);
        List<KeystoneListUsersResult> usersResults = HuaweicloudIamRepo.listUsers(cfg);
        System.out.println(usersResults);
    }

    @Test
    void certTest() {
        EdsHuaweicloudConfigModel.Huaweicloud cfg = getConfig(27);
        List<Certificates> certificates = HuaweicloudCcmRepo.listCertificates("eu-west-101", cfg);
        System.out.println(certificates);
    }

    public static final Region CN_NORTH_4 = new Region("cn-north-4", "https://scm.cn-north-4.myhuaweicloud.com");
    public static final Region AP_SOUTHEAST_1 = new Region("ap-southeast-1", "https://scm.ap-southeast-1.myhuaweicloud.com");

    @Test
    void scmTest() {
        EdsHuaweicloudConfigModel.Huaweicloud cfg = getConfig(27);
        List<com.huaweicloud.sdk.scm.v3.model.CertificateDetail> certificates = HuaweicloudScmRepo.listCertificates("eu-west-101", cfg);
        System.out.println(certificates);
    }

}
