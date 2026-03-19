package com.baiyi.cratos.eds.core.context;

import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/19 13:41
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class EdsAssetProviderContext {

    private final EdsAssetService edsAssetService;
    private final SimpleEdsFacade simpleEdsFacade;
    private final CredentialService credentialService;
    private final ConfigCredTemplate configCredTemplate;
    private final EdsAssetIndexFacade edsAssetIndexFacade;
    private final AssetToBusinessObjectUpdater assetToBusinessObjectUpdater;
    private final EdsProviderHolderFactory edsProviderHolderFactory;

}
