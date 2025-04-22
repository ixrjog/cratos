package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.identity.ResignationUsersProcessor;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/21 13:56
 * &#064;Version 1.0
 */
public class ResignationUsersProcessorTest extends BaseUnit {

    @Resource
    private ResignationUsersProcessor resignationUsersProcessor;

    @Test
    void resignationUsersTest() {
        resignationUsersProcessor.doTask();
    }

}
