package com.baiyi.cratos.service.factory;

import com.baiyi.cratos.service.base.BaseBusinessService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/1/12 10:26
 * @Version 1.0
 */
@Slf4j
public class BusinessServiceFactory {

    private BusinessServiceFactory() {
    }

    private static final Map<String, BaseBusinessService<?>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(BaseBusinessService<?> bean) {
        CONTEXT.put(bean.getBusinessType(), bean);
        log.debug("===================================== BusinessServiceFactory ======================================");
        log.debug("BusinessWrapperFactory Registered: serviceName={}, businessType={}", bean.getClass()
                .getSimpleName(), bean.getBusinessType());
    }

    public static BaseBusinessService<?> getService(String businessType) {
        if (CONTEXT.containsKey(businessType)) {
            return CONTEXT.get(businessType);
        }
        return null;
    }

}
