package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.alimail.model.AlimailDepartment;
import com.baiyi.cratos.eds.alimail.repo.AlimailDepartmentRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:58
 * &#064;Version 1.0
 */
public class EdsAlimailTest extends BaseEdsTest<EdsConfigs.Alimail> {


//    @Test
//    void test() {
//        EdsConfigs.Alimail alimail = getConfig(33);
//        String token = alimailTokenClient.getToken(alimail);
//        System.out.println(token);
//    }

    @Test
    void test2() {
        EdsConfigs.Alimail alimail = getConfig(33);
        List<AlimailDepartment.Department> departments = AlimailDepartmentRepo.listSubDepartments(alimail, "$root");
        System.out.println(departments);
    }

}
