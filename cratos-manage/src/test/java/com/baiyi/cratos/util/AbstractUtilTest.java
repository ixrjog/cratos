package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.AbstractUtil;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 下午1:48
 * &#064;Version 1.0
 */
public class AbstractUtilTest extends BaseUnit {

    @Test
    void test() {
        Map<String, String> map = Maps.newHashMap();
        map.put("k9", "k9");
        map.put("k2", "v2");
        map.put("k1", "v1");
        map.put("k3", "v3");
        map.put("k4", "v4");
        System.out.println(AbstractUtil.mapToString(map));
    }

}
