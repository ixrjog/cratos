package com.baiyi.cratos.common;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.enums.PermissionRoleEnum;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/19 10:18
 * @Version 1.0
 */
public class XTest extends BaseUnit {

    @Test
    void ddd(){
        PermissionRoleEnum e =  PermissionRoleEnum.valueOf("OWNER");
        System.out.println(e);
    }

}
