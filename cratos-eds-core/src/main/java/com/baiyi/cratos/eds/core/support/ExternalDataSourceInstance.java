package com.baiyi.cratos.eds.core.support;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/1/26 11:26
 * @Version 1.0
 */
@Data
public class ExternalDataSourceInstance<C extends IEdsConfigModel> {

    EdsInstance edsInstance;

    C edsConfig;

}
