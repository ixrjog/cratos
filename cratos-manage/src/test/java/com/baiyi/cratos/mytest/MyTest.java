package com.baiyi.cratos.mytest;

import com.baiyi.cratos.BaseUnit;
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
    void test2() {
        System.out.println(matchesDC("ddd-asdgdsg-dc1"));
        System.out.println(matchesDC("ddd-asdgdsgdc1"));
        System.out.println(matchesDC("ddd-asdgdsg-dc"));
        System.out.println(matchesDC("ddd-asdgdsg-dc22222"));
    }

    private boolean matchesDC(String name) {
        return name.matches(".*-dc\\d+");
    }

}
