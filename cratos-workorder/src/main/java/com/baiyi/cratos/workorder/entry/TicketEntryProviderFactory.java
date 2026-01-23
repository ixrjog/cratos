package com.baiyi.cratos.workorder.entry;

import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/14 10:56
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketEntryProviderFactory {

    /**
     * Map<WorkOrderKey, Map<BusinessType, TicketEntryProvider>>
     */
    private static final Map<String, Map<String, TicketEntryProvider<?, ?>>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(TicketEntryProvider<?, ?> provider) {
        if (CONTEXT.containsKey(provider.getKey())) {
            CONTEXT.get(provider.getKey())
                    .put(provider.getBusinessType(), provider);
        } else {
            CONTEXT.put(provider.getKey(), new ConcurrentHashMap<>());
            CONTEXT.get(provider.getKey())
                    .put(provider.getBusinessType(), provider);
        }
    }

//    @Deprecated
//    private static TicketEntryProvider<?, ?> getProvider(String key) {
//        return CONTEXT.get(key)
//                .values()
//                .stream()
//                .findFirst()
//                .orElse(null);
//    }

    public static TicketEntryProvider<?, ?> getProvider(String key, String businessType) {
        if (!CONTEXT.containsKey(key)) {
            return null;
        }
        return CONTEXT.get(key)
                .get(businessType);
    }

    public static TicketEntryProvider<?, ?> getProvider(WorkOrderKeys key, String businessType) {
        return getProvider(key.name(), businessType);
    }

}
