package com.baiyi.cratos.service.base;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 16:06
 * @Version 1.0
 */
public interface BaseService<T, M extends Mapper<T>> {

    M getMapper();

    default void add(T t) {
        getMapper().insert(t);
    }

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