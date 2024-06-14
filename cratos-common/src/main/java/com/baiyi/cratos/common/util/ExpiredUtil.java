package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/15 18:32
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ExpiredUtil {

    /**
     * 是否过期
     *
     * @param date
     * @return
     */
    public static boolean isExpired(Date date) {
        if (date == null) {
            return false;
        }
        long subTime = date.getTime() - System.currentTimeMillis();
        return subTime <= 0;
    }

    public static Date generateExpirationTime(long sourceDuration, TimeUnit sourceUnit) {
        return new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(sourceDuration, sourceUnit));
    }

}
