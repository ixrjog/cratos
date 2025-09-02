package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/26 14:07
 * &#064;Version 1.0
 */
public class RbacTest extends BaseUnit {

    @Resource
    private RbacRoleFacade rbacRoleFacade;

    @Test
    void test() {
        // 1 BASE 6 SRE
      //  RbacRoleVO.RoleDetails details = rbacRoleFacade.queryRoleDetailsV2(1);
       // System.out.println(details);
    }

}
