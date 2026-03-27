package com.baiyi.cratos.wrapper.factory;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
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
public class BusinessDecoratorFactory {

    private static final Map<String, BaseBusinessDecorator<?, ?>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(BaseBusinessDecorator<?, ?> bean) {
        CONTEXT.put(bean.getBusinessType(), bean);
        log.debug(StringFormatter.inDramaFormat("BusinessDecoratorFactory"));
        log.debug("BusinessDecoratorFactory Registered: beanName={}, businessType={}", bean.getClass()
                .getSimpleName(), bean.getBusinessType());
    }

    public static BaseBusinessDecorator<?, ?> getDecorator(String businessType) {
        if (CONTEXT.containsKey(businessType)) {
            return CONTEXT.get(businessType);
        }
        return null;
    }

}
