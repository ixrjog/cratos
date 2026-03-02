package com.baiyi.cratos.common.util;

import com.baiyi.cratos.domain.util.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/28 13:53
 * &#064;Version 1.0
 */
@Slf4j
public class SecurityLogger {

    private static final Logger SECURITY = LoggerFactory.getLogger("SECURITY");

    public static void log(String eventType, String user, String action, String resource) {
        Map<String, Object> event = Map.of(
                "timestamp", Instant.now().toString(),
                "event_type", eventType,
                "user", user,
                "action", action,
                "resource", resource
        );
        SECURITY.info(JSONUtils.writeValueAsString(event));
    }

}
