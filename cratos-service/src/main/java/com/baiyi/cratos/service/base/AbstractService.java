package com.baiyi.cratos.service.base;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 16:06
 * @Version 1.0
 */
public abstract class AbstractService<T, M extends Mapper<T>> implements BaseService<T> {

    abstract protected M getMapper();

    @Override
    public void add(T t) {
        getMapper().insert(t);
    }

    @Override
    public T getById(int id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKey(T t) {
        getMapper().updateByPrimaryKey(t);
    }

    @Override
    public void updateByPrimaryKeySelective(T t) {
        getMapper().updateByPrimaryKeySelective(t);
    }

    @Override
    public void deleteById(int id) {
        getMapper().deleteByPrimaryKey(id);
    }

    @Override
    public List<T> selectAll() {
        return getMapper().selectAll();
    }

}