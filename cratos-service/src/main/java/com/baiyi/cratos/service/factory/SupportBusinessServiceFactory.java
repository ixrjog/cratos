package com.baiyi.cratos.service.factory;

import com.baiyi.cratos.service.base.SupportBusinessService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/26 18:28
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class SupportBusinessServiceFactory {

    private static final Map<String, SupportBusinessService> CONTEXT = new ConcurrentHashMap<>();

    public static void register(SupportBusinessService bean) {
        CONTEXT.put(bean.getBusinessType(), bean);
    }

    public static SupportBusinessService getService(String businessType) {
        return CONTEXT.get(businessType);
    }

}
