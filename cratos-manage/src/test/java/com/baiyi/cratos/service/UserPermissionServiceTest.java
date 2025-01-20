package com.baiyi.cratos.service;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/20 10:10
 * &#064;Version 1.0
 */
public class UserPermissionServiceTest extends BaseUnit {

    @Resource
    private UserPermissionService userPermissionService;

    @Test
    void test() {
        List<Integer> ids = userPermissionService.queryUserPermissionBusinessIds("baiyi",
                BusinessTypeEnum.APPLICATION.name());
        System.out.println(ids);
    }

}
