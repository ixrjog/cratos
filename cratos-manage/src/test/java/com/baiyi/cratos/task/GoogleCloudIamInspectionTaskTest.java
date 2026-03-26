package com.baiyi.cratos.task;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.inspection.impl.GoogleCloudIamInspectionTask;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/26 10:58
 * &#064;Version 1.0
 */
public class GoogleCloudIamInspectionTaskTest extends BaseUnit {

    @Resource
    private GoogleCloudIamInspectionTask googleCloudIamInspectionTask;

    @Test
    void test() {
        googleCloudIamInspectionTask.inspectionTask();
    }

}
