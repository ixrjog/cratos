package com.baiyi.cratos.aspect.sensitive;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/1/8 14:51
 * @Version 1.0
 */
@Slf4j
public class SensitiveFormatterFactory {

    private SensitiveFormatterFactory() {
    }

    private static final Map<String, SensitiveFormatter> CONTEXT = new ConcurrentHashMap<>();

    public static void register(SensitiveFormatter formatter) {
        CONTEXT.put(formatter.getSensitiveType(), formatter);
        log.info("SensitiveFormatterFactory Registered: formatter={}, sensitiveType={}", formatter.getClass().getSimpleName(), formatter.getSensitiveType());
    }

    public static SensitiveFormatter getFormatter(String sensitiveType) {
        if (CONTEXT.containsKey(sensitiveType)) {
            return CONTEXT.get(sensitiveType);
        }
        return null;
    }

}
