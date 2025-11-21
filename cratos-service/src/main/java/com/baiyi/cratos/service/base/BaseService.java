package com.baiyi.cratos.service.base;

import com.baiyi.cratos.annotation.DomainDecrypt;
import com.baiyi.cratos.annotation.DomainEncrypt;
import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.util.BeanNameConverter;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.domain.util.SpringContextUtils;
import com.google.common.collect.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 16:06
 * @Version 1.0
 */
public interface BaseService<T extends HasIntegerPrimaryKey, M extends Mapper<T>> {

    @SuppressWarnings("unchecked")
    default M getMapper() {
        final String mapperBeanName = BeanNameConverter.serviceImplNameToMapperName(this.getClass()
                                                                                            .getSimpleName());
        return (M) SpringContextUtils.getBean(mapperBeanName);
    }

    // 方法映射
    @DomainEncrypt
    default void add(T record) {
        getMapper().insert(record);
    }

    // 方法映射
    @DomainDecrypt
    default T getById(Integer id) {
        return getMapper().selectByPrimaryKey(id);
    }

    // 方法映射
    default void updateByPrimaryKey(T record) {
        ((BaseService<?, ?>) AopContext.currentProxy()).clearCacheById(record.getId());
        getMapper().updateByPrimaryKey(record);
    }

    // 方法映射
    default void updateByPrimaryKeySelective(T record) {
        ((BaseService<?, ?>) AopContext.currentProxy()).clearCacheById(record.getId());
        getMapper().updateByPrimaryKeySelective(record);
    }

    // 方法映射
    default void deleteById(int id) {
        ((BaseService<?, ?>) AopContext.currentProxy()).clearCacheById(id);
        getMapper().deleteByPrimaryKey(id);
    }

    // CacheEvict
    void clearCacheById(int id);

    // 方法映射
    default List<T> selectAll() {
        return getMapper().selectAll();
    }

    default List<T> queryByIds(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Lists.newArrayList();
        }
        // 反射获取范型T的实体类
        Example example = new Example(Generics.find(this.getClass(), BaseService.class, 0));
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", idList);
        return getMapper().selectByExample(example);
    }

}