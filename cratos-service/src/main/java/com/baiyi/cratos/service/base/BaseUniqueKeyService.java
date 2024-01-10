package com.baiyi.cratos.service.base;

import com.baiyi.cratos.annotation.DomainDecrypt;

/**
 * @Author baiyi
 * @Date 2024/1/5 18:21
 * @Version 1.0
 */
public interface BaseUniqueKeyService<T> {

    @DomainDecrypt
    T getByUniqueKey(T t);

}