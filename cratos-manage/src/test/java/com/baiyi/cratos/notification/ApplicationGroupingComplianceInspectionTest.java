package com.baiyi.cratos.notification;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.inspection.impl.ApplicationGroupingComplianceInspection;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/25 上午10:35
 * &#064;Version 1.0
 */
public class ApplicationGroupingComplianceInspectionTest extends BaseUnit {

    @Resource
    private ApplicationGroupingComplianceInspection applicationGroupingComplianceInspection;

    @Test
    void test() {
        List<String> list = applicationGroupingComplianceInspection.toFilterList();
        System.out.println(list);
    }

}
