package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudEcsRepo;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudIamRepo;
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

}
