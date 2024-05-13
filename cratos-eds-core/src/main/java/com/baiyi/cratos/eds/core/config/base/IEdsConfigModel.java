package com.baiyi.cratos.eds.core.config.base;

import com.baiyi.cratos.domain.generator.EdsInstance;

/**
 * @Author baiyi
 * @Date 2024/2/26 10:54
 * @Version 1.0
 */
public interface IEdsConfigModel {

    EdsInstance getEdsInstance();

    void setEdsInstance(EdsInstance edsInstance);

}
