package com.baiyi.cratos.notification;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.inspection.impl.AppDeploymentImageInspection;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/27 10:56
 * &#064;Version 1.0
 */
public class AppDeploymentImageInspectionTest extends BaseUnit {

    @Resource
    private AppDeploymentImageInspection appDeploymentImageInspection;

    @Test
    void test() {
        appDeploymentImageInspection.inspectionTask();
    }

}
