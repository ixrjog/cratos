package com.baiyi.cratos.wrapper.factory;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/5 15:12
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class BusinessWrapperFactory {

    private static final Map<String, IBusinessWrapper<?, ?>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(IBusinessWrapper<?, ?> bean) {
        CONTEXT.put(bean.getBusinessType(), bean);
        log.debug(StringFormatter.inDramaFormat("BusinessWrapperFactory"));
        log.debug("BusinessWrapperFactory Registered: beanName={}, businessType={}", bean.getClass()
                .getSimpleName(), bean.getBusinessType());
    }

    public static IBusinessWrapper<?, ?> getWrapper(String businessType) {
        if (CONTEXT.containsKey(businessType)) {
            return CONTEXT.get(businessType);
        }
        return null;
    }

}
