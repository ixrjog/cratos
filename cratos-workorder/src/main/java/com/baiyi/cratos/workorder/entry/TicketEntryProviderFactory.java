package com.baiyi.cratos.workorder.entry;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 13:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketEntryProviderFactory {

    private static final Map<String, TicketEntryProvider<?,?>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(TicketEntryProvider<?,?> provider) {
        CONTEXT.put(provider.getKey(), provider);
    }

    public static TicketEntryProvider<?,?> getByProvider(String key) {
        return CONTEXT.get(key);
    }

}
