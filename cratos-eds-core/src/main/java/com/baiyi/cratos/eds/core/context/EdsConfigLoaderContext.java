package com.baiyi.cratos.eds.core.context;

import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/20 11:44
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class EdsConfigLoaderContext {

    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final EdsInstanceService edsInstanceService;
    private final EdsConfigService edsConfigService;
    private final ConfigCredTemplate configCredTemplate;
    private final CredentialService credService;
}
