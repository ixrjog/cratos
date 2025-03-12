package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.alimail.client.AlimailTokenClient;
import com.baiyi.cratos.eds.alimail.model.AlimailDepartment;
import com.baiyi.cratos.eds.alimail.repo.AlimailDepartmentRepo;
import com.baiyi.cratos.eds.core.config.EdsAlimailConfigModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:58
 * &#064;Version 1.0
 */
public class EdsAlimailTest extends BaseEdsTest<EdsAlimailConfigModel.Alimail> {

    @Resource
    private AlimailTokenClient alimailTokenClient;

    @Resource
    private AlimailDepartmentRepo alimailDepartmentRepo;

//    @Test
//    void test() {
//        EdsAlimailConfigModel.Alimail alimail = getConfig(33);
//        String token = alimailTokenClient.getToken(alimail);
//        System.out.println(token);
//    }

    @Test
    void test2() {
        EdsAlimailConfigModel.Alimail alimail = getConfig(33);
        List<AlimailDepartment.Department> departments = alimailDepartmentRepo.listSubDepartments(alimail, "$root");
        System.out.println(departments);
    }

}
