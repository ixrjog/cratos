package com.baiyi.cratos.service.base;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 15:59
 * @Version 1.0
 */
public interface BaseService<T> {

    void add(T t);

    T getById(int id);

    void updateByPrimaryKey(T t);

    void updateByPrimaryKeySelective(T t);

    void deleteById(int id);

    List<T> selectAll();

}