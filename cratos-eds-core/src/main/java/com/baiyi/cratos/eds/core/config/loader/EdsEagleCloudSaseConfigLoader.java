package com.baiyi.cratos.eds.core.config.loader;

import com.baiyi.cratos.eds.core.BaseEdsConfigLoader;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsConfigLoaderContext;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:36
 * &#064;Version 1.0
 */
@Component
public class EdsEagleCloudSaseConfigLoader extends BaseEdsConfigLoader<EdsConfigs.Sase> {

    public EdsEagleCloudSaseConfigLoader(EdsConfigLoaderContext context) {
        super(context);
    }

}
