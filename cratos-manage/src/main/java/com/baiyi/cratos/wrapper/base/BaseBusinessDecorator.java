package com.baiyi.cratos.wrapper.base;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.wrapper.factory.BusinessDecoratorFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/1/5 15:18
 * @Version 1.0
 */
public interface BaseBusinessDecorator<Business, VO> extends BaseWrapper<VO>, BaseBusiness.IBusinessTypeAnnotate, InitializingBean {

    void decorateBusiness(Business hasBusiness);

    default void afterPropertiesSet() {
        BusinessDecoratorFactory.register(this);
    }

}