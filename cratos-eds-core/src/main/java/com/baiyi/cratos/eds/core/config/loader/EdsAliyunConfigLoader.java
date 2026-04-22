package com.baiyi.cratos.eds.core.config.loader;

import com.baiyi.cratos.eds.core.BaseEdsConfigLoader;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsConfigLoaderContext;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/22 17:41
 * &#064;Version 1.0
 */
@Component
public class EdsAliyunConfigLoader extends BaseEdsConfigLoader<EdsConfigs.Aliyun> {

    public EdsAliyunConfigLoader(EdsConfigLoaderContext context) {
        super(context);
    }

}
