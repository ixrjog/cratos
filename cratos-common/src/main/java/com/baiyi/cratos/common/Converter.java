package com.baiyi.cratos.common;

import com.baiyi.cratos.common.util.BeanCopierUtil;

/**
 * @Author baiyi
 * @Date 2024/1/2 18:38
 * @Version 1.0
 */
public interface Converter<S, T>  {

    default T convert(S s, Class<T> targetClass) {
        return BeanCopierUtil.copyProperties(s, targetClass);
    }

}