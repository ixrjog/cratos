package com.baiyi.cratos.service.base;

import com.baiyi.cratos.annotation.DomainDecrypt;
import com.baiyi.cratos.annotation.DomainEncrypt;
import com.baiyi.cratos.domain.util.BeanNameConverter;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.domain.util.SpringContextUtil;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

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

    // 方法映射
    @DomainEncrypt
    default void add(T t) {
        getMapper().insert(t);
    }

    // 方法映射
    @DomainDecrypt
    default T getById(int id) {
        return getMapper().selectByPrimaryKey(id);
    }

    // 方法映射
    default void updateByPrimaryKey(T t) {
        getMapper().updateByPrimaryKey(t);
    }

    // 方法映射
    default void updateByPrimaryKeySelective(T t) {
        getMapper().updateByPrimaryKeySelective(t);
    }

    // 方法映射
    default void deleteById(int id) {
        getMapper().deleteByPrimaryKey(id);
    }

    // 方法映射
    default List<T> selectAll() {
        return getMapper().selectAll();
    }

    default List<T> queryByIds(List<Integer> ids) {
        // 反射获取范型T的实体类
        Example example = new Example(Generics.find(this.getClass(), BaseService.class, 0));
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);
        return getMapper().selectByExample(example);
    }

}