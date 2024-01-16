package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.ExpiredUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/1/16 10:01
 * @Version 1.0
 */
public class ExpiredUtilTest extends BaseUnit {

    @Test
    void test() {
        Date date = ExpiredUtil.generateExpirationTime(1L, TimeUnit.HOURS);
        System.out.println(date);
    }

    @Test
    void test2() {
        UUID uuid = UUID.randomUUID();
        String x = Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
        System.out.println(x);
    }

}
