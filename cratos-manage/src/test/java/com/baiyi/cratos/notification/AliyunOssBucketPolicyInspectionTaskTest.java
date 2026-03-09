package com.baiyi.cratos.notification;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.inspection.impl.AliyunOssBucketPolicyInspectionTask;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 13:42
 * &#064;Version 1.0
 */
public class AliyunOssBucketPolicyInspectionTaskTest extends BaseUnit {

    @Resource
    private AliyunOssBucketPolicyInspectionTask aliyunOssBucketPolicyInspectionTask;

    @Test
    void test() {
        aliyunOssBucketPolicyInspectionTask.inspectionTask();
    }

}
