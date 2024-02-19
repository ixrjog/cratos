package com.baiyi.cratos.service.base;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.service.factory.SupportBusinessServiceFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/1/30 10:46
 * @Version 1.0
 */
public interface SupportBusinessService extends BaseBusiness.IBusinessTypeAnnotate, InitializingBean {

    default void afterPropertiesSet() throws Exception {
        SupportBusinessServiceFactory.register(this);
    }

}
