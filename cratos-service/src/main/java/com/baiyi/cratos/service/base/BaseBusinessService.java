package com.baiyi.cratos.service.base;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.service.factory.BusinessServiceFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import tk.mybatis.mapper.common.Mapper;

import javax.persistence.Id;
import java.lang.reflect.Field;
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
        Class<?> targetClass = AopUtils.getTargetClass(record);
        for (Field field : targetClass.getDeclaredFields()) {
            if (AnnotationUtils.findAnnotation(field, Id.class) != null) {
                try {
                    field.setAccessible(true);
                    Object idValue = field.get(record);
                    if (idValue instanceof Integer) {
                        deleteById((Integer) idValue);
                    }
                } catch (IllegalAccessException ignored) {
                } finally {
                    field.setAccessible(false);
                }
                break;
            }
        }
    }

    default void deleteByBusiness(BaseBusiness.HasBusiness business) {
        selectByBusiness(business).forEach(this::delete);
    }

    default void afterPropertiesSet() {
        BusinessServiceFactory.register(this);
    }

}
