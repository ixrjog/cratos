package com.baiyi.cratos.business;

import com.baiyi.cratos.common.exception.UserPermissionBusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 11:13
 * &#064;Version 1.0
 */
@Slf4j
public class PermissionBusinessServiceFactory<T> {

    private static final Map<String, PermissionBusinessService<?>> CONTEXT = new ConcurrentHashMap<>();

    public static <T> void register(PermissionBusinessService<T> service) {
        CONTEXT.put(service.getBusinessType(), service);
    }

    public static void trySupport(String businessType) {
        if (!CONTEXT.containsKey(businessType)) {
            UserPermissionBusinessException.notSupported();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> PermissionBusinessService<T> getService(String businessType) {
        return (PermissionBusinessService<T>) CONTEXT.get(businessType);
    }

    public static Set<String> getBusinessTypes() {
        return CONTEXT.keySet();
    }

}
