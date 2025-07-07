package com.baiyi.cratos.common;

import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.domain.util.BeanCopierUtils;

/**
 * @Author baiyi
 * @Date 2024/1/2 18:38
 * @Version 1.0
 */
public interface Converter<S, T> {

    default T convert(S s) {
        return BeanCopierUtils.copyProperties(s, getTargetClazz());
    }

    @SuppressWarnings("unchecked")
    default Class<T> getTargetClazz() {
        //  return (Class<T>) AopUtils.getTargetClass(this).getAnnotation(TargetClazz.class).clazz();
        // 反射获取范型T的具体类型
        return Generics.find(this.getClass(), Converter.class, 1);
    }

}