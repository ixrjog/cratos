package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/6 16:06
 * &#064;Version 1.0
 */
public class HashCodeTest extends BaseUnit {

    @Test
    void test() {
        String name1 = "tms-itel";
        System.out.println(name1.hashCode());
        String name2 = "tms-sunmi";
        System.out.println(name2.hashCode());
    }

}
