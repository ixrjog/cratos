package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.googlecloud.repo.GcpProjectRepo;
import com.google.iam.admin.v1.Role;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/23 15:08
 * &#064;Version 1.0
 */
public class EdsGcpTest extends BaseEdsTest<EdsConfigs.Gcp> {

    @Resource
    private GcpProjectRepo gcpProjectRepo;

    @Test
    void test1() throws Exception {
        EdsConfigs.Gcp gcp = getConfig(23);
        List<Role> roles = gcpProjectRepo.listRoles(gcp);
        roles.forEach(System.out::println);
    }

}
