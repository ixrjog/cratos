package com.baiyi.cratos.service.factory;

import com.baiyi.cratos.service.base.BaseBusinessService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/12 10:26
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class BusinessServiceFactory {

    private static final Map<String, BaseBusinessService<?>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(BaseBusinessService<?> bean) {
        CONTEXT.put(bean.getBusinessType(), bean);
    }

    public static BaseBusinessService<?> getService(String businessType) {
        return CONTEXT.get(businessType);
    }

}
