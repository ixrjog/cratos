package com.baiyi.cratos.common;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.enums.PermissionRoleEnum;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/19 10:18
 * @Version 1.0
 */
@Slf4j
public class XTest extends BaseUnit {

    @Test
    void test1() {
        PermissionRoleEnum e = PermissionRoleEnum.valueOf("OWNER");
        System.out.println(e);
    }

    @Test
    void test2() {
        int i = 3 / 2;
        //System.out.println(i);
        log.info("Hello: {}", i);
        System.out.println(StringFormatter.inDramaFormat("Hello"));
    }

}
