package com.baiyi.cratos.service.base;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.service.factory.BusinessServiceFactory;
import org.springframework.beans.factory.InitializingBean;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 10:18
 * @Version 1.0
 */
public interface BaseBusinessService<T extends HasIntegerPrimaryKey> extends BaseService<T, Mapper<T>>, BaseBusiness.IBusinessTypeAnnotate, InitializingBean {

    List<T> selectByBusiness(BaseBusiness.HasBusiness business);

    default void delete(T record) {
        if (record == null) return;
        if (IdentityUtils.hasIdentity(record.getId())) {
            deleteById(record.getId());
        }
    }

    default void deleteByBusiness(BaseBusiness.HasBusiness business) {
        selectByBusiness(business).forEach(this::delete);
    }

    default void afterPropertiesSet() {
        BusinessServiceFactory.register(this);
    }

}
