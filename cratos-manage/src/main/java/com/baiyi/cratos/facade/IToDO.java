package com.baiyi.cratos.facade;

import com.baiyi.cratos.common.util.BeanCopierUtil;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:36
 * @Version 1.0
 */
public interface IToDO<D> {

    default D to(Object param) {
        return BeanCopierUtil.copyProperties(param, getToClass());
    }

    Class<D> getToClass();

}
