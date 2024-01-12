package com.baiyi.cratos.service.base;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.service.factory.BusinessServiceFactory;
import org.springframework.beans.factory.InitializingBean;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 10:18
 * @Version 1.0
 */
public interface BaseBusinessService<T> extends BaseService<T, Mapper<T>>, BaseBusiness.IBusinessTypeAnnotate, InitializingBean {

    List<T> selectByBusiness(BaseBusiness.IBusiness business);

    void delete(T t);

    default void deleteByBusiness(BaseBusiness.IBusiness business) {
        selectByBusiness(business).forEach(this::delete);
    }

    default void afterPropertiesSet() {
        BusinessServiceFactory.register(this);
    }

}
