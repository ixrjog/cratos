package com.baiyi.cratos.service.base;

import com.baiyi.cratos.annotation.DomainEncrypt;
import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.exception.DaoServiceException;
import tk.mybatis.mapper.common.Mapper;

import java.util.Objects;

/**
 * @Author baiyi
 * @Date 2024/1/5 18:21
 * @Version 1.0
 */
public interface BaseUniqueKeyService<T extends HasIntegerPrimaryKey, M extends Mapper<T>> extends BaseService<T, M>, HasUniqueKey<T> {

    // 方法映射
    @DomainEncrypt
    @Override
    default void add(T record) {
        if (record == null) {
            throw new DaoServiceException("The insertion record does not exist.");
        }
        T existRecord = getByUniqueKey(record);
        if (Objects.nonNull(existRecord)) {
            throw new DaoServiceException("Union key conflict.");
        }
        try {
            getMapper().insert(record);
        } catch (Exception e) {
            throw new DaoServiceException(e.getMessage());
        }
    }

    // 方法映射
    @DomainEncrypt
    default void save(T record) {
        if (record == null) {
            throw new DaoServiceException("The insertion record does not exist.");
        }
        T existRecord = getByUniqueKey(record);
        if (Objects.isNull(existRecord)) {
            getMapper().insert(record);
        } else {
            record.setId(existRecord.getId());
            getMapper().updateByPrimaryKey(record);
        }
    }

}