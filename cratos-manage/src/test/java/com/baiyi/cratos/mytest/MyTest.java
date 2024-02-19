package com.baiyi.cratos.mytest;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.enums.DocumentTypeEnum;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/2/13 21:47
 * @Version 1.0
 */
public class MyTest extends BaseUnit {

    @Test
    void test() {
        int loop = 0;
        while (true) {
        }

    }

    @Test
    void test2(){
        DocumentTypeEnum.valueOf("abc");
    }

}
