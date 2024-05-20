package com.baiyi.cratos.common;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.configuration.RbacAutoConfigInitializer;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/24 09:50
 * @Version 1.0
 */
public class AutoTest extends BaseUnit {

    @Resource
    private RbacAutoConfigInitializer rbacResourceAutomaticConfiguration;

    @Test
    void test() {
        rbacResourceAutomaticConfiguration.start();
    }

}
