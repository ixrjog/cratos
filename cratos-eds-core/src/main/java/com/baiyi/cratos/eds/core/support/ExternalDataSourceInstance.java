package com.baiyi.cratos.eds.core.support;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/1/26 11:26
 * @Version 1.0
 */
@Data
@Builder
public class ExternalDataSourceInstance<Config extends IEdsConfigModel> {

    EdsInstance edsInstance;

    Config edsConfigModel;

    public Config getConfig() {
        return edsConfigModel;
    }

}
