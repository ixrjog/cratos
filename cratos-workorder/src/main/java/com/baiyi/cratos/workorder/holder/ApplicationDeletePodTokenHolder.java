package com.baiyi.cratos.workorder.holder;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.workorder.holder.token.ApplicationDeletePodToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/9 17:18
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationDeletePodTokenHolder {

    private final RedisUtil redisUtil;

    private static final String KEY = "APPLICATION:DELETE:POD:TOKEN:USERNAME:{}:APPLICATION_NAME:{}";

    private static final long DEFAULT_EXPIRE = 2; // 2 hours

    public ApplicationDeletePodToken.Token getToken(String username, String applicationName) {
        String key = StringFormatter.arrayFormat(KEY, username, applicationName);

        if (redisUtil.hasKey(key)) {
            return (ApplicationDeletePodToken.Token) redisUtil.get(key);
        }
        return ApplicationDeletePodToken.Token.NO_TOKEN;
    }

    public void setToken(String username, String applicationName, Integer ticketId) {
        ApplicationDeletePodToken.Token.builder()
                .username(username)
                .applicationName(applicationName)
                .ticketId(ticketId)
                // 2 hours
                .expires(ExpiredUtil.generateExpirationTime(DEFAULT_EXPIRE, TimeUnit.HOURS))
                .build();
    }

}
