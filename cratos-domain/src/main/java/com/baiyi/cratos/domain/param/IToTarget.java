package com.baiyi.cratos.domain.param;

import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.util.Generics;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:47
 * @Version 1.0
 */
public interface IToTarget<T> {

    default T toTarget() {
        return BeanCopierUtil.copyProperties(this, getTargetClazz());
    }

    @SuppressWarnings("unchecked")
    default Class<T> getTargetClazz() {
        //  return (Class<T>) AopUtils.getTargetClass(this).getAnnotation(TargetClazz.class).clazz();
        // 反射获取范型T的具体类型
        return Generics.find(this.getClass(), IToTarget.class, 0);
    }

}