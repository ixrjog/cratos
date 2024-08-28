package com.baiyi.cratos.service.base;

import com.baiyi.cratos.annotation.DomainDecrypt;
import lombok.NonNull;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/28 11:33
 * &#064;Version 1.0
 */
public interface HasUniqueKey<T> {

    @DomainDecrypt
    T getByUniqueKey(@NonNull T record);

}
