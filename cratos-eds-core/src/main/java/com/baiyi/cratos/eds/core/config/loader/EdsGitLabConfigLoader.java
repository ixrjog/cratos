package com.baiyi.cratos.eds.core.config.loader;

import com.baiyi.cratos.eds.core.BaseEdsConfigLoader;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsConfigLoaderContext;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 11:20
 * &#064;Version 1.0
 */
@Component
public class EdsGitLabConfigLoader extends BaseEdsConfigLoader<EdsConfigs.GitLab> {

    public EdsGitLabConfigLoader(EdsConfigLoaderContext context) {
        super(context);
    }

}
