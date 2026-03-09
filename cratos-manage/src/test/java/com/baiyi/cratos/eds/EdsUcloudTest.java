package com.baiyi.cratos.eds;

import cn.ucloud.uhost.models.DescribeUHostInstanceResponse;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.ucloud.global.repo.UcloudGlobalUHostRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/6 11:21
 * &#064;Version 1.0
 */
public class EdsUcloudTest extends BaseEdsTest<EdsConfigs.Ucloud> {

    /**
     *     https://docs.ucloud.cn/api/summary/regionlist
     */
    @Test
    void test2() throws Exception {
        EdsConfigs.Ucloud ucloud = getConfig(60);
        List<DescribeUHostInstanceResponse.UHostInstanceSet> result = UcloudGlobalUHostRepo.describeUHostInstance(
               "jpn-tky", ucloud, null);
        System.out.println(result);
    }

}