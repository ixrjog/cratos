package com.baiyi.cratos.eds.context;

import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/20 14:53
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class CloudComputerOperatorContext {

    private final EdsInstanceService edsInstanceService;
    private final EdsConfigService edsConfigService;
    private final EdsAssetService edsAssetService;
    private final CredentialService credentialService;
    private final ConfigCredTemplate configCredTemplate;

}
