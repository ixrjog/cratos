package com.baiyi.cratos.common.util;

import com.baiyi.cratos.domain.generator.Env;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/24 10:36
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvLifecycleUtils {

    public static final int MAX_LIFECYCLE_DAYS = 360;

    public static Date generateExpiredTimeWithEnvLifecycle(Date expiredTime, String envName,
                                                           Map<String, Env> envLifecycleMap) {
        if (!envLifecycleMap.containsKey(envName)) {
            return Objects.requireNonNullElseGet(expiredTime,
                    () -> ExpiredUtils.generateExpirationTime(MAX_LIFECYCLE_DAYS, TimeUnit.DAYS));
        }
        Env envLifecycle = envLifecycleMap.get(envName);
        Date maxExpiredTime = ExpiredUtils.generateExpirationTime(envLifecycle.getLifecycle(), TimeUnit.DAYS);
        if (expiredTime == null) {
            return maxExpiredTime;
        }
        return expiredTime.getTime() > maxExpiredTime.getTime() ? maxExpiredTime : expiredTime;
    }

}
