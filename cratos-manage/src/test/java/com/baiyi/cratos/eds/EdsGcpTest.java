package com.baiyi.cratos.eds;

import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.googlecloud.enums.GcpIAMMemberType;
import com.baiyi.cratos.eds.googlecloud.repo.GcpApiKeysRepo;
import com.baiyi.cratos.eds.googlecloud.repo.GcpProjectRepo;
import com.baiyi.cratos.util.GcpMemberUtils;
import com.google.api.apikeys.v2.Key;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/23 15:08
 * &#064;Version 1.0
 */
public class EdsGcpTest extends BaseEdsTest<EdsConfigs.Gcp> {

    @Resource
    private GcpMemberUtils gcpMemberUtils;

    @Resource
    private GcpProjectRepo gcpProjectRepo;

    @Resource
    private GcpApiKeysRepo gcpApiKeysRepo;

    @Test
    void test1() throws Exception {
        gcpMemberUtils.printAllGcpMemberTable();
    }

    @Test
    void test2() throws Exception {
        EdsConfigs.Gcp googleCloud = getConfig(23);
        gcpProjectRepo.addMember(googleCloud, GcpIAMMemberType.USER, "xiaogangzhang98@gmail.com", "roles/browser");
    }

    @Test
    void test3() throws Exception {
        EdsConfigs.Gcp googleCloud = getConfig(23);
        //  gcpProjectRepo.grantRole(config, "user:baiyi@palmpay-inc.com", "roles/storage.admin");
        gcpProjectRepo.grantRole(
                googleCloud, GcpIAMMemberType.USER, "xiaogangzhang98@gmail.com", "roles/storage.admin");
    }

    @Test
    void test4() throws IOException {
        EdsConfigs.Gcp googleCloud = getConfig(23);
        List<Key> keys = gcpApiKeysRepo.listApiKeys(googleCloud);
        System.out.println(JSONUtils.writeValueAsString(keys));
    }

    @Test
    void test5() throws IOException {
        EdsConfigs.Gcp googleCloud = getConfig(23);
        String key = gcpApiKeysRepo.getKeyString(
                googleCloud, "projects/1018069716389/locations/global/keys/82cc8bfb-f573-4e14-a7d7-e00bbde6f858");
        System.out.println(key);
    }


}
