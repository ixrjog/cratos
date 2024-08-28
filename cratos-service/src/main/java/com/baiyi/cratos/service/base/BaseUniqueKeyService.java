package com.baiyi.cratos.service.base;

import com.baiyi.cratos.annotation.DomainEncrypt;
import com.baiyi.cratos.exception.DaoServiceException;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author baiyi
 * @Date 2024/1/5 18:21
 * @Version 1.0
 */
public interface BaseUniqueKeyService<T, M extends Mapper<T>> extends BaseService<T, M>, HasUniqueKey<T> {

    // 方法映射
    @DomainEncrypt
    @Override
    default void add(T record) {
        if (record == null) {
            throw new DaoServiceException("The insertion record does not exist.");
        }
        if (getByUniqueKey(record) != null) {
            throw new DaoServiceException("Union key conflict.");
        }
        getMapper().insert(record);
    }

}