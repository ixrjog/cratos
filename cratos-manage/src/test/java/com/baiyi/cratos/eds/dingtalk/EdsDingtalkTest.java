package com.baiyi.cratos.eds.dingtalk;

import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkDepartmentModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkUserModel;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkDepartmentParam;
import com.baiyi.cratos.eds.dingtalk.repo.DingtalkDepartmentRepo;
import com.baiyi.cratos.eds.dingtalk.repo.DingtalkUserRepo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/12 16:48
 * &#064;Version 1.0
 */
public class EdsDingtalkTest extends BaseEdsTest<EdsDingtalkConfigModel.Dingtalk> {

    @Resource
    private DingtalkDepartmentRepo dingtalkDepartmentRepo;

    @Resource
    private DingtalkUserRepo dingtalkUserRepo;

    @Test
    void test2() {
        EdsDingtalkConfigModel.Dingtalk dingtalk = getConfig(43);
        DingtalkDepartmentModel.GetDepartmentResult result = dingtalkDepartmentRepo.get(dingtalk,
                DingtalkDepartmentParam.GetDepartment.builder()
                        .build());
        System.out.println(result);
    }

    @Test
    void test3() {
        EdsDingtalkConfigModel.Dingtalk dingtalk = getConfig(17);

        DingtalkUserModel.GetUser result = dingtalkUserRepo.getUser(dingtalk, "2432404506-2088177608");

        System.out.println(result);
    }

    @Test
    void test4() {
        EdsDingtalkConfigModel.Dingtalk dingtalk = getConfig(43);

        String result = dingtalkUserRepo.getUserTest(dingtalk, "52336726782698");

        System.out.println(result);
    }

}
