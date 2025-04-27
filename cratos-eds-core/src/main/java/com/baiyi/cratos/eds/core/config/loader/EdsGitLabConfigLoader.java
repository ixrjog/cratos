package com.baiyi.cratos.eds.core.config.loader;

import com.baiyi.cratos.eds.core.BaseEdsConfigLoader;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 11:20
 * &#064;Version 1.0
 */
@Component
public class EdsGitLabConfigLoader extends BaseEdsConfigLoader<EdsGitLabConfigModel.GitLab> {

    public EdsGitLabConfigLoader(EdsInstanceProviderHolderBuilder holderBuilder, EdsInstanceService edsInstanceService,
                                 EdsConfigService edsConfigService, ConfigCredTemplate configCredTemplate,
                                 CredentialService credService) {
        super(holderBuilder, edsInstanceService, edsConfigService, configCredTemplate, credService);
    }

}
