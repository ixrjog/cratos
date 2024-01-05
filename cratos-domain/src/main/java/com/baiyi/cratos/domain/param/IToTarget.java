package com.baiyi.cratos.domain.param;

import com.baiyi.cratos.domain.util.BeanCopierUtil;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:47
 * @Version 1.0
 */
public interface IToTarget<T> {

    default T toTarget() {
        return BeanCopierUtil.copyProperties(this, getTargetClazz());
    }

    Class<T> getTargetClazz();

}