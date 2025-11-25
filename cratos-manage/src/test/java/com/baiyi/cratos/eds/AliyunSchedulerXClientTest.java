package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.eds.aliyun.client.AliyunSchedulerXClient;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/25 15:54
 * &#064;Version 1.0
 */
public class AliyunSchedulerXClientTest extends BaseUnit {

    @Test
    void test1() throws Exception {
        AliyunSchedulerXClient.run();
    }
}
