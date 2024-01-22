package com.baiyi.cratos.service.base;

import com.baiyi.cratos.annotation.DomainDecrypt;
import com.baiyi.cratos.annotation.DomainEncrypt;
import com.baiyi.cratos.domain.util.BeanNameConverter;
import com.baiyi.cratos.domain.util.SpringContextUtil;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 16:06
 * @Version 1.0
 */

public interface BaseService<T, M extends Mapper<T>> {

    @SuppressWarnings("unchecked")
    default M getMapper() {
        final String mapperBeanName = BeanNameConverter.serviceImplNameToMapperName(this.getClass()
                .getSimpleName());
        return (M) SpringContextUtil.getBean(mapperBeanName);
    }

    @DomainEncrypt
    default void add(T t) {
        getMapper().insert(t);
    }

    @DomainDecrypt
    default T getById(int id) {
        return getMapper().selectByPrimaryKey(id);
    }

    default void updateByPrimaryKey(T t) {
        getMapper().updateByPrimaryKey(t);
    }

    default void updateByPrimaryKeySelective(T t) {
        getMapper().updateByPrimaryKeySelective(t);
    }

    default void deleteById(int id) {
        getMapper().deleteByPrimaryKey(id);
    }

    default List<T> selectAll() {
        return getMapper().selectAll();
    }

}