package com.baiyi.cratos.wrapper.base;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.wrapper.factory.BusinessWrapperFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/1/5 15:18
 * @Version 1.0
 */
public interface BaseBusinessWrapper<B, T> extends BaseWrapper<T>, BaseBusiness.IBusinessTypeAnnotate, InitializingBean {

    void businessWrap(B b);

    default void afterPropertiesSet() {
        BusinessWrapperFactory.register(this);
    }

}