package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.model.EdsHcsConfigModel;
import com.baiyi.cratos.eds.huaweicloud.stack.repo.HcsEcsRepo;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/28 16:20
 * &#064;Version 1.0
 */
public class HcsRepoTest extends BaseEdsTest<EdsHcsConfigModel.Hcs> {

    @Test
    void test1() {
        EdsHcsConfigModel.Hcs hcs = getConfig(41);
        HcsEcsRepo.listServers(hcs)
                .forEach(server -> System.out.println(server.getName()));
    }

}