package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.eds.computer.CloudComputerOperatorFactory;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 14:32
 * &#064;Version 1.0
 */
public class CloudComputerOperatorFactoryTest extends BaseUnit {

    @Test
    void test1() {
        // 112037 i-gw86y71wjxdf7i0uw4qc lo-rocketmq-dashboard-dev
        CloudComputerOperatorFactory.stop(EdsAssetTypeEnum.ALIYUN_ECS.name(), 112037);
    }

    @Test
    void test2() {
        // 112037 i-gw86y71wjxdf7i0uw4qc lo-rocketmq-dashboard-dev
        CloudComputerOperatorFactory.start(EdsAssetTypeEnum.ALIYUN_ECS.name(), 112037);
    }

    @Test
    void test3() {
        // 112037 i-gw86y71wjxdf7i0uw4qc lo-rocketmq-dashboard-dev
        CloudComputerOperatorFactory.reboot(EdsAssetTypeEnum.ALIYUN_ECS.name(), 112037);
    }

}
